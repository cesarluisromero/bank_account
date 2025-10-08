package com.bootcamp.bank_account.infrastructure.out.persistence.document;

import com.bootcamp.bank_account.domain.model.AccountType;
import com.bootcamp.bank_account.domain.model.Active;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Document("accounts")
public class AccountDocument {
    @Id private String id;
    private String customerId;
    private AccountType type;
    private String currency;
    @Indexed(unique = true)
    private String accountNumber;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal balance;
    private Active activeInactive;
}
