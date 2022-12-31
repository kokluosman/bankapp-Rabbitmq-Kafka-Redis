package com.example.bankapp.api;

import com.example.bankapp.dto.CreateCustomerRequest;
import com.example.bankapp.dto.CustomerDto;
import com.example.bankapp.dto.UpdateCustomerRequest;
import com.example.bankapp.service.CustomerManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManager customerManager;

    @PostMapping(path = "createCustomer")
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerRequest customerRequest){
        CustomerDto customer = this.customerManager.createCustomer(customerRequest);
        return ResponseEntity.ok(customer);
    }

    @GetMapping(path = "/getallCustomer")
    private ResponseEntity<List<CustomerDto>> getAllCustomer(){
        List<CustomerDto> allCustomer = this.customerManager.getAllCustomer();
        return ResponseEntity.status(HttpStatus.OK).body(allCustomer);
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(name = "id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(this.customerManager.getCustomerDtoById(id));
    }

    @DeleteMapping(path = "deleteCustomer/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable(name = "id")String id){
        this.customerManager.deleteCustomer(id);
        return ResponseEntity.ok("Customer was deleted!!");
    }

    @PutMapping(path = "updateCustomer/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody UpdateCustomerRequest
                                                                  customerRequest,
                                                      @PathVariable(name = "id") String id){
        CustomerDto customerDto = this.customerManager.updateCustomer(id, customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
    }



}
