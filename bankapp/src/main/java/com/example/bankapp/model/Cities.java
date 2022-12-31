package com.example.bankapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisHash;

@Data
@EqualsAndHashCode
@RedisHash("cities")
@Entity
public class Cities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer plateCode;
    private String name;



}
