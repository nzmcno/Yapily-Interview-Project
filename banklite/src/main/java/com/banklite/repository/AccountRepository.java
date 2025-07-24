package com.banklite.repository;

import com.banklite.model.Account;
import com.banklite.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByAccountHolderNameContaining(String name);
    
    List<Account> findByCurrency(Currency currency);
}