package com.banklite.service;

import com.banklite.exception.AccountNotFoundException;
import com.banklite.model.Account;
import com.banklite.model.Currency;
import com.banklite.model.dto.AccountRequest;
import com.banklite.model.dto.AccountResponse;
import com.banklite.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    @Test
    void shouldCreateAccount() {
        // Given
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName("John Doe");
        request.setBalance(new BigDecimal("1000.00"));
        request.setCurrency(Currency.USD);
        
        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setAccountHolderName("John Doe");
        savedAccount.setAccountNumber("ACC123456789");
        savedAccount.setBalance(new BigDecimal("1000.00"));
        savedAccount.setCurrency(Currency.USD);
        savedAccount.setCreatedAt(LocalDateTime.now());
        
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        
        // When
        AccountResponse response = accountService.createAccount(request);
        
        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getAccountHolderName()).isEqualTo("John Doe");
        assertThat(response.getBalance()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(response.getCurrency()).isEqualTo(Currency.USD);
        assertThat(response.getAccountNumber()).isEqualTo("ACC123456789");
        
        verify(accountRepository).save(any(Account.class));
    }
    
    @Test
    void shouldGetAccountById() {
        // Given
        Account account = new Account();
        account.setId(1L);
        account.setAccountHolderName("Jane Doe");
        account.setAccountNumber("ACC987654321");
        account.setBalance(new BigDecimal("500.00"));
        account.setCurrency(Currency.EUR);
        account.setCreatedAt(LocalDateTime.now());
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        // When
        AccountResponse response = accountService.getAccount(1L);
        
        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getAccountHolderName()).isEqualTo("Jane Doe");
        assertThat(response.getBalance()).isEqualTo(new BigDecimal("500.00"));
        assertThat(response.getCurrency()).isEqualTo(Currency.EUR);
        
        verify(accountRepository).findById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> accountService.getAccount(999L))
            .isInstanceOf(AccountNotFoundException.class)
            .hasMessage("Account not found with id: 999");
        
        verify(accountRepository).findById(999L);
    }
    
    @Test
    void shouldGetAllAccounts() {
        // Given
        Account account1 = createTestAccount(1L, "John Doe", "ACC111");
        Account account2 = createTestAccount(2L, "Jane Doe", "ACC222");
        
        when(accountRepository.findAll()).thenReturn(List.of(account1, account2));
        
        // When
        List<AccountResponse> responses = accountService.getAllAccounts();
        
        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getAccountHolderName()).isEqualTo("John Doe");
        assertThat(responses.get(1).getAccountHolderName()).isEqualTo("Jane Doe");
        
        verify(accountRepository).findAll();
    }
    
    @Test
    void shouldUpdateAccount() {
        // Given
        Account existingAccount = createTestAccount(1L, "John Doe", "ACC111");
        
        AccountRequest updateRequest = new AccountRequest();
        updateRequest.setAccountHolderName("John Smith");
        updateRequest.setBalance(new BigDecimal("2000.00"));
        updateRequest.setCurrency(Currency.GBP);
        
        Account updatedAccount = createTestAccount(1L, "John Smith", "ACC111");
        updatedAccount.setBalance(new BigDecimal("2000.00"));
        updatedAccount.setCurrency(Currency.GBP);
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
        
        // When
        AccountResponse response = accountService.updateAccount(1L, updateRequest);
        
        // Then
        assertThat(response.getAccountHolderName()).isEqualTo("John Smith");
        assertThat(response.getBalance()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(response.getCurrency()).isEqualTo(Currency.GBP);
        
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }
    
    @Test
    void shouldDeleteAccount() {
        // Given
        when(accountRepository.existsById(1L)).thenReturn(true);
        
        // When
        accountService.deleteAccount(1L);
        
        // Then
        verify(accountRepository).existsById(1L);
        verify(accountRepository).deleteById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentAccount() {
        // Given
        when(accountRepository.existsById(999L)).thenReturn(false);
        
        // When & Then
        assertThatThrownBy(() -> accountService.deleteAccount(999L))
            .isInstanceOf(AccountNotFoundException.class)
            .hasMessage("Account not found with id: 999");
        
        verify(accountRepository).existsById(999L);
        verify(accountRepository, never()).deleteById(any());
    }
    
    private Account createTestAccount(Long id, String name, String accountNumber) {
        Account account = new Account();
        account.setId(id);
        account.setAccountHolderName(name);
        account.setAccountNumber(accountNumber);
        account.setBalance(new BigDecimal("1000.00"));
        account.setCurrency(Currency.USD);
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }
}