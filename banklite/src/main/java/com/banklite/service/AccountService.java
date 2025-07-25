package com.banklite.service;

import com.banklite.exception.AccountNotFoundException;
import com.banklite.model.Account;
import com.banklite.model.dto.AccountRequest;
import com.banklite.model.dto.AccountResponse;
import com.banklite.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();
        account.setAccountHolderName(request.getAccountHolderName());
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(request.getBalance());
        account.setCurrency(request.getCurrency());
        
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }
    
    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return mapToResponse(account);
    }
    
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
            
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getBalance());
        account.setCurrency(request.getCurrency());
        
        Account updated = accountRepository.save(account);
        return mapToResponse(updated);
    }
    
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }
    
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
    
    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}