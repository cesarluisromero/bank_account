package com.bootcamp.bank_account.infrastructure.in.web.dto;

import com.bootcamp.bank_account.domain.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true) @NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;

    @NotNull
    private CustomerType type;

    @NotBlank
    private String documentNumber;

    private String fullName;     // si PERSONAL
    private String businessName; // si ENTERPRISE
    private String email;
    private List<String> phones;
}
