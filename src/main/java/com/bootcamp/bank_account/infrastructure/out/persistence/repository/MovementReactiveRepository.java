package com.bootcamp.bank_account.infrastructure.out.persistence.repository;

import com.bootcamp.bank_account.infrastructure.out.persistence.document.MovementDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovementReactiveRepository extends ReactiveMongoRepository<MovementDocument, String> {
    Flux<MovementDocument> findByAccountId(String accountId);
}
