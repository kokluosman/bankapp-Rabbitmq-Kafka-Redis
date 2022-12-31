package com.example.bankapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class Account implements Serializable {

    @Id
    private String id;
    private String customerId;
    private City city;
    private Currency currency;
    private double balance;


}
