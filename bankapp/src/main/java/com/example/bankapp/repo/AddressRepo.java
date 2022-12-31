package com.example.bankapp.repo;

import com.example.bankapp.model.Address;
import com.example.bankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,String> {
}
