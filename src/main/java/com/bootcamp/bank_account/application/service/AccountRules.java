package com.bootcamp.bank_account.application.service;

import com.bootcamp.bank_account.domain.model.Account;
import com.bootcamp.bank_account.domain.model.AccountType;
import com.bootcamp.bank_account.domain.model.CustomerType;
import com.bootcamp.bank_account.domain.port.out.AccountRepositoryPort;
import com.bootcamp.bank_account.infrastructure.in.web.dto.CustomerDto;
import com.bootcamp.bank_account.infrastructure.out.persistence.repository.AccountReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// application/service/AccountRules.java
@Component
@RequiredArgsConstructor
public class AccountRules {
    private final AccountReactiveMongoRepository accountRepo;

    public Mono<Void> validate(CustomerDto customer, Account input) {
        if (customer.getType() == CustomerType.PERSONAL) {
            if (input.getType() == AccountType.SAVINGS) {
                return accountRepo.countByCustomerIdAndType(input.getCustomerId(), AccountType.SAVINGS)
                        .flatMap(c -> c > 0 ? Mono.error(new IllegalStateException("Personal ya tiene una cuenta de ahorro"))
                                : Mono.empty());
            } else if (input.getType() == AccountType.CURRENT) {
                return accountRepo.countByCustomerIdAndType(input.getCustomerId(), AccountType.CURRENT)
                        .flatMap(c -> c > 0 ? Mono.error(new IllegalStateException("Personal ya tiene una cuenta corriente"))
                                : Mono.empty());
            } else if (input.getType() == AccountType.FIXED_TERM) {
                return Mono.empty(); // múltiples permitidas
            } else {
                return Mono.error(new IllegalStateException("Tipo de cuenta no soportado para PERSONAL"));
            }
        } else if (customer.getType() == CustomerType.ENTERPRISE) {
            if (input.getType() != AccountType.CURRENT) {
                return Mono.error(new IllegalStateException("Empresarial solo puede tener cuentas corrientes"));
            }
            // (opcional) valida titulares/firmantes si tu modelo lo requiere
            return Mono.empty();
        } else {
            return Mono.error(new IllegalStateException("Tipo de cliente desconocido"));
        }
    }
}

