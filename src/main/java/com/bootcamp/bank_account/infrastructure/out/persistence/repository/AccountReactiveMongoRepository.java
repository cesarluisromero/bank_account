package com.bootcamp.bank_account.infrastructure.out.persistence.repository;

import com.bootcamp.bank_account.domain.model.AccountType;
import com.bootcamp.bank_account.infrastructure.out.persistence.document.AccountDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountReactiveMongoRepository extends ReactiveMongoRepository<AccountDocument, String> {
    Mono<AccountDocument> findByAccountNumber(String accountNumber);
    Flux<AccountDocument> findByCustomerId(String customerId);
    Mono<Long> countByCustomerIdAndType(String customerId, AccountType type);
}
