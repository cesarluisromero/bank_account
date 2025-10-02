package com.bootcamp.bank_account.domain.port.out;

import com.bootcamp.bank_account.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementRepositoryPort {
    Mono<Movement> save(Movement m);
    Flux<Movement> findByAccountId(String accountId);
}
