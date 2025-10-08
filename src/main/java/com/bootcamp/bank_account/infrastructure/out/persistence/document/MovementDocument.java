package com.bootcamp.bank_account.infrastructure.out.persistence.document;

import com.bootcamp.bank_account.domain.model.MovementType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document("movements")
public class MovementDocument {
    @Id private String id;
    private String accountId;
    private MovementType type;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    @Field("timestamp")
    private Instant at;
    private String description;
}
