package com.example.bankapp.service;

import com.example.bankapp.dto.CreateCustomerRequest;
import com.example.bankapp.dto.CustomerDto;
import com.example.bankapp.dto.CustomerDtoConverter;
import com.example.bankapp.dto.UpdateCustomerRequest;
import com.example.bankapp.model.City;
import com.example.bankapp.model.Customer;
import com.example.bankapp.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerManager {

    private final CustomerRepo customerRepo;
    private final CustomerDtoConverter customerDtoConverter;


    public CustomerDto createCustomer(CreateCustomerRequest customerRequest){
        Customer customer = new Customer();
        customer.setId(customer.getId());
        customer.setName(customer.getName());
        customer.setCity(City.valueOf(customer.getCity().name()));
        customer.setDateOfBirth(customer.getDateOfBirth());
        customer.setAddress(customer.getAddress());
        this.customerRepo.save(customer);
        return customerDtoConverter.convertToDto(customer);
    }

    public List<CustomerDto> getAllCustomer(){
        List<Customer> customers = this.customerRepo.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for (Customer customer: customers){
            CustomerDto customerDto = new CustomerDto();
            customerDto.setAddress(customer.getAddress());
            customerDto.setId(customer.getId());
            customerDto.setName(customer.getName());
            customerDto.setDateOfBirth(customer.getDateOfBirth());
            customerDto.setCity(City.valueOf(customer.getCity().name()));
            customerDtos.add(customerDto);
        }
        return customerDtos.stream()
                .map(customerDto -> {
                    customerDtoConverter.convertToDto((Customer) customers);
                    return customerDto;
                }).collect(Collectors.toList());
    }

    @Transactional
    public CustomerDto getCustomerDtoById(String id){
        Optional<Customer> customerOptional = this.customerRepo.findById(id);
        return customerOptional.map(customerDtoConverter::convertToDto).orElse(new CustomerDto());
    }

    public void deleteCustomer(String id){
        this.customerRepo.deleteById(id);
    }

    @Transactional
    public CustomerDto updateCustomer(String id, UpdateCustomerRequest customerRequest){
        Optional<Customer> optionalCustomer = this.customerRepo.findById(id);

        optionalCustomer.ifPresent(customer -> {
            customer.setName(customerRequest.getName());
            customer.setCity(City.valueOf(customerRequest.getCity().name()));
            customer.setAddress(customerRequest.getAddress());
            customer.setDateOfBirth(customerRequest.getDateOfBirth());
            this.customerRepo.save(customer);
        });
        return optionalCustomer.map(customerDtoConverter::convertToDto).orElse(new CustomerDto());
    }

    protected Customer getCustomerById(String id){
        return this.customerRepo.findById(id).orElse(new Customer());
    }

}
