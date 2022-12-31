package com.example.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest extends BaseCustomerRequest{

    @NotBlank(message = "Customer id must not be empty")
    private String id;

}
