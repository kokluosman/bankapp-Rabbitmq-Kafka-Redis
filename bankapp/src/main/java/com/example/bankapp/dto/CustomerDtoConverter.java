package com.example.bankapp.dto;

import com.example.bankapp.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoConverter {

    public CustomerDto convertToDto(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerDto.getId());
        customerDto.setName(customerDto.getName());
        customerDto.setCity(customerDto.getCity());
        customerDto.setDateOfBirth(customerDto.getDateOfBirth());
        customerDto.setAddress(customerDto.getAddress());

        return customerDto;
    }
}
