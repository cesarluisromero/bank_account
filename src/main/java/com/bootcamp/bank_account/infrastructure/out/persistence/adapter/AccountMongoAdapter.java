package com.bootcamp.bank_account.infrastructure.out.persistence.adapter;

import com.bootcamp.bank_account.domain.model.*;
import com.bootcamp.bank_account.domain.port.out.*;
import com.bootcamp.bank_account.infrastructure.out.persistence.document.*;
import com.bootcamp.bank_account.infrastructure.out.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountMongoAdapter implements AccountRepositoryPort, MovementRepositoryPort {

    private final AccountReactiveRepository accountRepo;
    private final MovementReactiveRepository movementRepo;

    private static Account toDomain(AccountDocument d) {
        return Account.builder()
                .id(d.getId()).customerId(d.getCustomerId()).type(d.getType()).currency(d.getCurrency())
                .accountNumber(d.getAccountNumber()).balance(d.getBalance()).active(d.isActive()).build();
    }
    private static AccountDocument toDoc(Account a) {
        return AccountDocument.builder()
                .id(a.getId()).customerId(a.getCustomerId()).type(a.getType()).currency(a.getCurrency())
                .accountNumber(a.getAccountNumber()).balance(a.getBalance()).active(a.isActive()).build();
    }

    private static Movement toDomain(MovementDocument d) {
        return Movement.builder()
                .id(d.getId()).accountId(d.getAccountId()).type(d.getType()).amount(d.getAmount())
                .at(d.getAt()).description(d.getDescription()).build();
    }
    private static MovementDocument toDoc(Movement m) {
        return MovementDocument.builder()
                .id(m.getId()).accountId(m.getAccountId()).type(m.getType()).amount(m.getAmount())
                .at(m.getAt()).description(m.getDescription()).build();
    }

    // Account
    @Override public Mono<Account> save(Account a) { return accountRepo.save(toDoc(a)).map(AccountMongoAdapter::toDomain); }
    @Override public Mono<Account> findById(String id) { return accountRepo.findById(id).map(AccountMongoAdapter::toDomain); }
    @Override public Mono<Account> findByAccountNumber(String n) { return accountRepo.findByAccountNumber(n).map(AccountMongoAdapter::toDomain); }
    @Override public Flux<Account> findByCustomerId(String c) { return accountRepo.findByCustomerId(c).map(AccountMongoAdapter::toDomain); }

    // Movement
    @Override public Mono<Movement> save(Movement m) { return movementRepo.save(toDoc(m)).map(AccountMongoAdapter::toDomain); }
    @Override public Flux<Movement> findByAccountId(String aId) { return movementRepo.findByAccountId(aId).map(AccountMongoAdapter::toDomain); }
}
