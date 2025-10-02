package com.bootcamp.bank_account.domain.model;

import lombok.*;


import java.math.BigDecimal;

@Data @Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor
public class Account {
    private String id;
    private String customerId;
    private AccountType type;
    private String currency;
    private String accountNumber;     // único
    private BigDecimal balance;       // >= 0
    private boolean active;
}
