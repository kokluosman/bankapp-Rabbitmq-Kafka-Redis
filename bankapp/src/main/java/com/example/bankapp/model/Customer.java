package com.example.bankapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    private String id;

    private String name;
    private Integer dateOfBirth;
    private City city;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    private Address address;
}
