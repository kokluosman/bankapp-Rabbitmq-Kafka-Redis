package com.example.bankapp.repo;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account,String> {

    //"select * from where balance>$(balance)"
    List<Account> findAllByBalanceGreaterThan(Double balance);

    //select * from account where currency=$(currency) and başance <100
    List<Account> findAllByCurrencyIsAndBalanceLessThan(Currency currency,Double balance);
}
