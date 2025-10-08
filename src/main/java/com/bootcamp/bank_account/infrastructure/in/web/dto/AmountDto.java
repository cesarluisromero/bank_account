package com.bootcamp.bank_account.infrastructure.in.web.dto;

import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

public record AmountDto(

        @NotNull @Positive BigDecimal amount,
        String description) {

}
