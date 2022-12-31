package com.example.bankapp;

import com.example.bankapp.model.*;
import com.example.bankapp.repo.AccountRepo;
import com.example.bankapp.repo.AddressRepo;
import com.example.bankapp.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.List;

@SpringBootApplication
@EnableCaching
@RequiredArgsConstructor
public class BankappApplication implements CommandLineRunner {

    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;

    public static void main(String[] args) {
        SpringApplication.run(BankappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Customer c1 = Customer.builder()
                .name("Osman")
                .id("1234568")
                .address(Address.builder()
                        .city(City.ISTANBUL)
                        .postCode("465312")
                        .addressDetails("Bu bir adres detayıdır.")
                        .build())
                .city(City.ISTANBUL)
                .dateOfBirth(1978)
                .build();

        Customer c2 = Customer.builder()
                .name("Semih")
                .id("789456")
                .address(Address.builder()
                        .city(City.MANISA)
                        .postCode("465312")
                        .addressDetails("Bu bir adres detayıdır 2.")
                        .build())
                .city(City.MANISA)
                .dateOfBirth(1980)
                .build();

        Customer c3 = Customer.builder()
                .name("Zeynep")
                .id("456238")
                .address(Address.builder()
                        .city(City.IZMIR)
                        .postCode("465312")
                        .addressDetails("Bu bir adres detayıdır 3.")
                        .build())
                .city(City.IZMIR)
                .dateOfBirth(1985)
                .build();

        customerRepo.saveAll(List.of(c1,c2,c3));

        Account a1 = Account.builder()
                .id("100")
                .customerId("1234568")
                .city(City.ISTANBUL)
                .balance(1230)
                .currency(Currency.TL)
                .build();

        Account a2 = Account.builder()
                .id("101")
                .customerId("789456")
                .city(City.MANISA)
                .balance(7898)
                .currency(Currency.TL)
                .build();

        Account a3 = Account.builder()
                .id("102")
                .customerId("456238")
                .city(City.IZMIR)
                .balance(120000)
                .currency(Currency.TL)
                .build();

        accountRepo.saveAll(List.of(a1,a2,a3));
    }
}
