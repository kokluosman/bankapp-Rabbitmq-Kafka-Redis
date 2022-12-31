package com.example.bankapp.dto;

import com.example.bankapp.model.City;
import com.example.bankapp.model.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAccountRequest {

    @NotBlank(message = "Customer id must not be null")
    private String customerId;
    @NotNull
    @Min(0)
    private double balance;
    @NotNull(message = "Currency must not be null")
    private Currency currency;
    @NotNull(message = "City must not be null")
    private City city;

}
