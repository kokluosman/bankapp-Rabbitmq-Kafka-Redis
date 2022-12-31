package com.example.bankapp.repo;

import com.example.bankapp.model.Cities;
import com.example.bankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesRepo extends JpaRepository<Cities,Long> {
}
