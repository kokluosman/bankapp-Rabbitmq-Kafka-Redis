package com.example.bankapp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitiesDto {
    private Long id;
    private Integer plateCode;
    private String name;
}
