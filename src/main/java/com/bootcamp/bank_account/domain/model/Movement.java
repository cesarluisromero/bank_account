package com.bootcamp.bank_account.domain.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Movement {
    private String id;
    private String accountId;
    private MovementType type;
    private BigDecimal amount;
    @Field("timestamp")
    private Instant at;
    private String description;
}
