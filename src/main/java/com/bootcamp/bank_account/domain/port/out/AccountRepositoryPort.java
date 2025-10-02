package com.bootcamp.bank_account.domain.port.out;

import com.bootcamp.bank_account.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepositoryPort {
    Mono<Account> save(Account a);
    Mono<Account> findById(String id);
    Mono<Account> findByAccountNumber(String number);
    Flux<Account> findByCustomerId(String customerId);
}
