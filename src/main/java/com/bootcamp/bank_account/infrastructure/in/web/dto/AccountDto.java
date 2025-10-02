package com.bootcamp.bank_account.infrastructure.in.web.dto;

import com.bootcamp.bank_account.domain.model.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountDto(
        String id,
        @NotBlank String customerId,
        @NotNull AccountType type,
        @NotBlank String currency,
        @NotBlank String accountNumber,
        @PositiveOrZero BigDecimal balance
) {}
