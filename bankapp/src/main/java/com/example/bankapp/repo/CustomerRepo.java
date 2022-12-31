package com.example.bankapp.repo;

import com.example.bankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,String> {
}
