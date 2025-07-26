package com.banklite.controller;

import com.banklite.model.Currency;
import com.banklite.model.dto.AccountRequest;
import com.banklite.model.dto.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AccountControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateAndGetAccount() {
        // Create account
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName("John Doe");
        request.setBalance(new BigDecimal("1000.00"));
        request.setCurrency(Currency.USD);
        
        ResponseEntity<AccountResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/accounts", request, AccountResponse.class);
        
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().getAccountHolderName()).isEqualTo("John Doe");
        assertThat(createResponse.getBody().getBalance()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(createResponse.getBody().getCurrency()).isEqualTo(Currency.USD);
        assertThat(createResponse.getBody().getAccountNumber()).startsWith("ACC");
        
        // Get account
        Long accountId = createResponse.getBody().getId();
        ResponseEntity<AccountResponse> getResponse = restTemplate.getForEntity(
            "/api/v1/accounts/" + accountId, AccountResponse.class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(accountId);
        assertThat(getResponse.getBody().getAccountHolderName()).isEqualTo("John Doe");
    }
    
    @Test
    void shouldGetAllAccounts() {
        // Create two accounts
        AccountRequest request1 = createAccountRequest("Alice Smith", "500.00", Currency.EUR);
        AccountRequest request2 = createAccountRequest("Bob Johnson", "750.00", Currency.GBP);
        
        restTemplate.postForEntity("/api/v1/accounts", request1, AccountResponse.class);
        restTemplate.postForEntity("/api/v1/accounts", request2, AccountResponse.class);
        
        // Get all accounts
        ResponseEntity<AccountResponse[]> response = restTemplate.getForEntity(
            "/api/v1/accounts", AccountResponse[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    void shouldUpdateAccount() {
        // Create account
        AccountRequest createRequest = createAccountRequest("Original Name", "100.00", Currency.USD);
        ResponseEntity<AccountResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/accounts", createRequest, AccountResponse.class);
        
        Long accountId = createResponse.getBody().getId();
        
        // Update account
        AccountRequest updateRequest = createAccountRequest("Updated Name", "200.00", Currency.EUR);
        restTemplate.put("/api/v1/accounts/" + accountId, updateRequest);
        
        // Verify update
        ResponseEntity<AccountResponse> getResponse = restTemplate.getForEntity(
            "/api/v1/accounts/" + accountId, AccountResponse.class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getAccountHolderName()).isEqualTo("Updated Name");
        assertThat(getResponse.getBody().getBalance()).isEqualTo(new BigDecimal("200.00"));
        assertThat(getResponse.getBody().getCurrency()).isEqualTo(Currency.EUR);
    }
    
    @Test
    void shouldDeleteAccount() {
        // Create account
        AccountRequest request = createAccountRequest("To Be Deleted", "300.00", Currency.USD);
        ResponseEntity<AccountResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/accounts", request, AccountResponse.class);
        
        Long accountId = createResponse.getBody().getId();
        
        // Delete account
        restTemplate.delete("/api/v1/accounts/" + accountId);
        
        // Verify deletion - should return 500 (since GlobalExceptionHandler is disabled)
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
            "/api/v1/accounts/" + accountId, String.class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @Test
    void shouldReturn500ForNonExistentAccount() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/v1/accounts/99999", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private AccountRequest createAccountRequest(String name, String balance, Currency currency) {
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName(name);
        request.setBalance(new BigDecimal(balance));
        request.setCurrency(currency);
        return request;
    }
}