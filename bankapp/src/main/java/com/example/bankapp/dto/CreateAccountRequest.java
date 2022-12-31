package com.example.bankapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAccountRequest extends BaseAccountRequest{

    @NotBlank(message = "Account id must not be empty")
    private String id;


}
