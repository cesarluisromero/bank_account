package com.bootcamp.bank_account.domain.port.in;

import com.bootcamp.bank_account.domain.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

public interface AccountUseCase {
    Mono<Account> create(Account toCreate);
    Mono<Account> findById(String id);
    Flux<Account> listByCustomer(String customerId);

    Mono<Account> deposit(String accountId, BigDecimal amount, String description);
    Mono<Account> withdraw(String accountId, BigDecimal amount, String description);
    Mono<Void> transfer(String fromId, String toId, BigDecimal amount, String description);

    Flux<Movement> movements(String accountId);
}