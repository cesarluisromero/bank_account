package com.bootcamp.bank_account.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Movement {
    private String id;
    private String accountId;
    private MovementType type;
    private BigDecimal amount;     // > 0
    private Instant at;
    private String description;
}
