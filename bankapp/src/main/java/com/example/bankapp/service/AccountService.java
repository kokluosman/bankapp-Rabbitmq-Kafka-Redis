package com.example.bankapp.service;

import com.example.bankapp.dto.*;
import com.example.bankapp.exception.CustomerNotFoundException;
import com.example.bankapp.model.Account;
import com.example.bankapp.model.City;
import com.example.bankapp.model.Customer;
import com.example.bankapp.repo.AccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepo accountRepo;
    private final CustomerManager customerManager;
    private final AccountDtoConverter accountDtoConverter;
    private final DirectExchange exchange;
    private final AmqpTemplate rabbitTemplate;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Value("firstRoute")
    String routingKey;

    @Value("firstStepQueue")
    String queueName;

    @CachePut(value = "account",key = "#id")
    public AccountDto createAccount(CreateAccountRequest createAccountRequest){
        Customer customer = customerManager.getCustomerById(createAccountRequest.getCustomerId());
        if (customer.getId() == null || customer.getId().trim().equals("")){
            throw new CustomerNotFoundException("Customer not found!!");
        }

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .city(City.valueOf(createAccountRequest.getCity().name()))
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .build();
        return accountDtoConverter.convertToAccountDto(this.accountRepo.save(account));
    }

    @CacheEvict(value = "account",allEntries = true)
    public AccountDto updateAccount(String id,UpdateAccountRequest accountRequest){
        Customer customer = customerManager.getCustomerById(accountRequest.getCustomerId());
        if (customer.getId().equals("") || customer.getId()==null){
            return AccountDto.builder().build();
        }
        Optional<Account> byId = this.accountRepo.findById(id);
        byId.ifPresent(account -> {
            account.setBalance(accountRequest.getBalance());
            account.setCity(accountRequest.getCity());
            account.setCurrency(accountRequest.getCurrency());
            account.setCustomerId(accountRequest.getCustomerId());
            this.accountRepo.save(account);
        });

        return byId.map(accountDtoConverter::convertToAccountDto).orElse(new AccountDto());
    }


    @Cacheable(value = "account")
    public List<AccountDto> getAllAccountDto(){
        List<Account> accountList = this.accountRepo.findAll();

        return accountList.stream()
                .map(accountDtoConverter::convertToAccountDto)
                .collect(Collectors.toList());
    }


    public AccountDto getAccountById(String id){
        return accountRepo.findById(id)
                .map(accountDtoConverter::convertToAccountDto)
                .orElse(new AccountDto());
    }

    @CacheEvict(value = "account",allEntries = true)
    public void deleteAccount(String id){
        this.accountRepo.deleteById(id);
    }

    public AccountDto withDrawMoney(String id,Double amount){
        Optional<Account> accountOptional = this.accountRepo.findById(id);
        accountOptional.ifPresent(account -> {
            if (account.getBalance() >= amount && account.getBalance()>0){
                account.setBalance(account.getBalance()-amount);
                accountRepo.save(account);
            }else {
                System.out.println("Insufficent funds -> accountId: " +id+
                        "+ balance: " + account.getBalance()+
                        "+ amount: " + amount);
            }
        });
        return accountOptional.map(accountDtoConverter::convertToAccountDto).orElse(new AccountDto());
    }

    public AccountDto addMoney(String id , Double amount){
        Optional<Account> optionalAccount = this.accountRepo.findById(id);
        optionalAccount.ifPresent(account -> {
            account.setBalance(account.getBalance()+amount);
            this.accountRepo.save(account);
        });
        return optionalAccount.map(accountDtoConverter::convertToAccountDto).orElse(new AccountDto());
    }

    public void transferMoney(MoneyTransferRequest transferRequest){
        rabbitTemplate.convertAndSend(exchange.getName(),routingKey,transferRequest);
    }
    @Transactional
    @RabbitListener(queues = "firstStepQueue")
    public void transferMoneyMessage(MoneyTransferRequest transferRequest){
        Optional<Account> accountOptional = this.accountRepo.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
            if (account.getBalance() >= transferRequest.getAmount() && account.getBalance() > 0){
                account.setBalance(account.getBalance()- transferRequest.getAmount());
                accountRepo.save(account);
                rabbitTemplate.convertAndSend(exchange.getName(),"secondRoute",transferRequest);
            }else {
                System.out.println("Insufficent funds -> accountId: " +transferRequest.getFromId()+
                        "+ balance: " + account.getBalance()+
                        "+ amount: " + transferRequest.getAmount());
            }},
                () -> System.out.println("Account Not Found")

                );
    }
    @Transactional
    @RabbitListener(queues = "secondStepQueue")
    public void updateReceiverAccount(MoneyTransferRequest transferRequest){
        Optional<Account> accountOptional = this.accountRepo.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse(account -> {
                    if (account.getBalance() >= transferRequest.getAmount() && account.getBalance() > 0){
                        account.setBalance(account.getBalance() + transferRequest.getAmount());
                        accountRepo.save(account);
                        rabbitTemplate.convertAndSend(exchange.getName(),"thirdRoute",transferRequest);
                    }
        },
                () ->{
            System.out.println("Receiver Acccount Not Found");
            Optional<Account> senderAccount = accountRepo.findById(transferRequest.getFromId());
            senderAccount.ifPresent(sender -> {
                System.out.println("Money charge back to sender!");
                sender.setBalance(sender.getBalance() + transferRequest.getAmount());
                accountRepo.save(sender);
            });
        });
    }
    @Transactional
    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(MoneyTransferRequest transferRequest){
        Optional<Account> accountOptional = this.accountRepo.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
            String notificationMessage =
                    "Dear Customer %s \n Your money transfer request" +
                            " has been succeed. " +
                            "Your new balance is %s";
            String senderMessage =
                    String.format(notificationMessage,account.getId(),account.getBalance());
            kafkaTemplate.send("Transfer-Notification",senderMessage);
        },
                ()-> System.out.println("Account Not Found"));

        Optional<Account> optionalAccount = this.accountRepo.findById(transferRequest.getToId());
        optionalAccount.ifPresentOrElse(account -> {
            String notificationMessage =
                    "Dear Customer %s \n Your received a money transfer from %s" +
                            "Your new balance is %s";
            System.out.println("Receiver("+account.getId()+") new account balance: "+ account.getBalance());
            String receiverMessage =
                    String.format(notificationMessage,account.getId(),account.getBalance());
            kafkaTemplate.send("Transfer-Notification",receiverMessage);
        },()-> System.out.println("Account Not Found"));
    }
}
