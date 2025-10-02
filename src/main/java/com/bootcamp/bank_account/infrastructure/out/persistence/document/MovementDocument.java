package com.bootcamp.bank_account.infrastructure.out.persistence.document;

import com.bootcamp.bank_account.domain.model.MovementType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document("movements")
public class MovementDocument {
    @Id private String id;
    private String accountId;
    private MovementType type;
    private BigDecimal amount;
    private Instant at;
    private String description;
}
