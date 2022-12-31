package com.example.bankapp.service;

import com.example.bankapp.dto.AccountDto;
import com.example.bankapp.dto.AccountDtoConverter;
import com.example.bankapp.dto.CreateAccountRequest;
import com.example.bankapp.model.*;
import com.example.bankapp.repo.AccountRepo;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
@Import({AccountService.class,CustomerManager.class})
class AccountServiceTest {

    private AccountService accountService;
    private AccountRepo accountRepo;
    private CustomerManager customerManager;
    private AccountDtoConverter accountDtoConverter;
    private DirectExchange exchange;
    private AmqpTemplate rabbitTemplate;
    private KafkaTemplate kafkaTemplate;

    @BeforeEach
    void Setup() throws Exception{

        accountRepo= Mockito.mock(AccountRepo.class);
        customerManager= Mockito.mock(CustomerManager.class);
        accountDtoConverter= Mockito.mock(AccountDtoConverter.class);
        exchange= Mockito.mock(DirectExchange.class);
        rabbitTemplate= Mockito.mock(AmqpTemplate.class);
        kafkaTemplate= Mockito.mock(KafkaTemplate.class);

        accountService = new AccountService(accountRepo,customerManager,
                accountDtoConverter,exchange,rabbitTemplate,kafkaTemplate);

    }

    @Test
    public void whenCreateAccountCalledWithValidRequest_itShouldReturnValidAccountDto(){

        CreateAccountRequest createAccountRequest = generateAccountRequest();
        Customer customer = generateCustomer();
        Account account = generateAccount(createAccountRequest);
        AccountDto accountDto = generateAccountDto();

        //Determine mock services behavior test scenerio
        Mockito.when(customerManager.getCustomerById("12345")).thenReturn(customer);
        Mockito.when(accountRepo.save(account)).thenReturn(account);
        Mockito.when(accountDtoConverter.convertToAccountDto(account)).thenReturn(accountDto);

        //Call the testing Method
        AccountDto result =accountService.createAccount(createAccountRequest);

        //Check result and verify the mock methods are called
        Assert.assertEquals(result,accountDto);
        Mockito.verify(customerManager.getCustomerById("12345"));
        Mockito.verify(accountRepo.save(account));
        Mockito.verify(accountDtoConverter.convertToAccountDto(account));


    }


    @Test
    public void whenCreateAccountCalledWithNonExistCustomer_itShouldThrowRuntimeException(){

        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("12345");
        createAccountRequest.setBalance(100);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TL);

        //Determine mock services behavior test scenerio
        Mockito.when(customerManager.getCustomerById("12345")).thenReturn(Customer.builder().build());

        //Call the testing Method
        AccountDto expectedAccountDto = AccountDto.builder().build();

        AccountDto result = accountService.createAccount(createAccountRequest);

        //Check result and verify the mock methods are called
        Assert.assertEquals(result,expectedAccountDto);
        Mockito.verifyNoInteractions(accountRepo);
        Mockito.verifyNoInteractions(accountDtoConverter);


    }


    @Test
    public void whenCreateAccountCalledAndRepositoryThrewException_itShouldThrowException(){

        CreateAccountRequest createAccountRequest = new CreateAccountRequest("1234");
        createAccountRequest.setCustomerId("12345");
        createAccountRequest.setBalance(100);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TL);

        Customer customer = Customer.builder()
                .name("Osman")
                .id("12345")
                .address(Address.builder()
                        .city(City.ISTANBUL)
                        .addressDetails("This is an address details !")
                        .postCode("123520")
                        .build())
                .dateOfBirth(1999)
                .city(City.ISTANBUL)
                .build();

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .city(City.valueOf(createAccountRequest.getCity().name()))
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .build();

        //Determine mock services behavior test scenerio
        Mockito.when(customerManager.getCustomerById("12345")).thenReturn(customer);
        Mockito.when(accountRepo.save(account)).thenThrow(new RuntimeException("This test Exception my wrote!"));

        accountService.createAccount(createAccountRequest);


        Mockito.verify(customerManager).getCustomerById("12345");
        Mockito.verify(accountRepo.save(account));
        Mockito.verifyNoInteractions(accountDtoConverter);


    }

    private CreateAccountRequest generateAccountRequest(){
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("12345");
        createAccountRequest.setCustomerId("12345");
        createAccountRequest.setBalance(100);
        createAccountRequest.setCity(City.ISTANBUL);
        createAccountRequest.setCurrency(Currency.TL);
        return createAccountRequest;
    }

    private Customer generateCustomer(){
        return Customer.builder()
                .id("12345")
                .address(Address.builder()
                        .city(City.ISTANBUL)
                        .postCode("456312")
                        .addressDetails("This is an address details")
                        .build())
                .city(City.ISTANBUL)
                .dateOfBirth(1999)
                .name("Osman")
        .build();
    }

    private Account generateAccount(CreateAccountRequest accountRequest){
        return Account.builder()
                .id(accountRequest.getId())
                .balance(accountRequest.getBalance())
                .currency(accountRequest.getCurrency())
                .customerId(accountRequest.getCustomerId())
                .city(accountRequest.getCity())
                .build();
    }

    private AccountDto generateAccountDto(){
        return AccountDto.builder()
                .id("1234")
                .customerId("12345")
                .currency(Currency.TL)
                .balance(100.0)
                .build();
    }
}