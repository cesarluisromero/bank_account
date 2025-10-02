package com.bootcamp.bank_account.infrastructure.in.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AmountDto(@NotNull @Positive BigDecimal amount, String description) {}
