package com.banklite.model.dto;

import com.banklite.model.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    
    private Long id;
    private String accountHolderName;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime createdAt;
}