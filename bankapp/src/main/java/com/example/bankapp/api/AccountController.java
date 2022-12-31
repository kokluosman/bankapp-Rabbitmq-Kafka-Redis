package com.example.bankapp.api;

import com.example.bankapp.dto.AccountDto;
import com.example.bankapp.dto.CreateAccountRequest;
import com.example.bankapp.dto.MoneyTransferRequest;
import com.example.bankapp.dto.UpdateAccountRequest;
import com.example.bankapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService  accountService;


    @GetMapping(path = "/getAllAccount")
    public ResponseEntity<List<AccountDto>> getAllAccount(){
        return ResponseEntity.ok(this.accountService.getAllAccountDto());
    }

    @GetMapping(path = "/getAccountById/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") String  id){
        return ResponseEntity.ok(this.accountService.getAccountById(id));
    }

    @PostMapping(path = "/createAccount")
    public ResponseEntity<Object> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        return ResponseEntity.ok(this.accountService.createAccount(createAccountRequest));
    }
    @PutMapping(path = "/updateAccount/{id}")
    public ResponseEntity<AccountDto> updateAccount(@Valid @RequestBody UpdateAccountRequest accountRequest,
                                                    @PathVariable(name = "id") String id){
        AccountDto accountDto = this.accountService.updateAccount(id, accountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }
    @DeleteMapping(path = "/deleteAccount/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable(name = "id")String id){
        this.accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withDrawMoney(@PathVariable(name = "id")String id,
                                                    @PathVariable(name = "amount")Double amount){
        AccountDto accountDto = this.accountService.withDrawMoney(id, amount);
        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }
    @PutMapping(path = "/add/{id}/{amount}")
    public ResponseEntity<AccountDto> addMoney(@PathVariable(name = "id")String id,
                                               @PathVariable(name = "amount") Double amount){
        AccountDto accountDto = this.accountService.addMoney(id, amount);
        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }
    @PutMapping(path = "/transfer")
    public ResponseEntity<String> transferMoney(@Valid @RequestBody MoneyTransferRequest transferRequest){
        this.accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("Your transaction has been completed successfully !!");
    }
}
