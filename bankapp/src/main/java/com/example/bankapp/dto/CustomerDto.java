package com.example.bankapp.dto;

import com.example.bankapp.model.Address;
import com.example.bankapp.model.City;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private String id;
    private String name;
    private Integer dateOfBirth;
    private City city;
    private Address address;
}
