package com.bootcamp.bank_account.application;

import com.bootcamp.bank_account.domain.model.*;
import com.bootcamp.bank_account.domain.port.in.AccountUseCase;
import com.bootcamp.bank_account.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountUseCase {

    private final AccountRepositoryPort accountRepo;
    private final MovementRepositoryPort movementRepo;

    @Override
    public Mono<Account> create(Account input) {
        // cuenta única por accountNumber
        return accountRepo.findByAccountNumber(input.getAccountNumber())
                .flatMap(a -> Mono.error(new IllegalStateException("accountNumber already exists")))
                .switchIfEmpty(
                        accountRepo.save(input.toBuilder()
                                .active(true)
                                .balance(input.getBalance() == null ? BigDecimal.ZERO : input.getBalance())
                                .build()
                        )
                ).cast(Account.class);
    }

    @Override public Mono<Account> findById(String id) { return accountRepo.findById(id); }
    @Override public Flux<Account> listByCustomer(String customerId) { return accountRepo.findByCustomerId(customerId); }

    @Transactional  // funciona en Atlas/replica set
    @Override
    public Mono<Account> deposit(String accountId, BigDecimal amount, String description) {
        if (amount == null || amount.signum() <= 0) return Mono.error(new IllegalArgumentException("amount>0"));
        return accountRepo.findById(accountId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("account not found")))
                .flatMap(acc -> {
                    acc.setBalance(acc.getBalance().add(amount));
                    Movement m = Movement.builder()
                            .accountId(acc.getId())
                            .type(MovementType.DEPOSIT)
                            .amount(amount)
                            .at(Instant.now())
                            .description(description)
                            .build();
                    return movementRepo.save(m).then(accountRepo.save(acc));
                });
    }

    @Transactional
    @Override
    public Mono<Account> withdraw(String accountId, BigDecimal amount, String description) {
        if (amount == null || amount.signum() <= 0) return Mono.error(new IllegalArgumentException("amount>0"));
        return accountRepo.findById(accountId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("account not found")))
                .flatMap(acc -> {
                    if (acc.getBalance().compareTo(amount) < 0) return Mono.error(new IllegalStateException("insufficient funds"));
                    acc.setBalance(acc.getBalance().subtract(amount));
                    Movement m = Movement.builder()
                            .accountId(acc.getId())
                            .type(MovementType.WITHDRAW)
                            .amount(amount)
                            .at(Instant.now())
                            .description(description)
                            .build();
                    return movementRepo.save(m).then(accountRepo.save(acc));
                });
    }

    @Transactional
    @Override
    public Mono<Void> transfer(String fromId, String toId, BigDecimal amount, String description) {
        return withdraw(fromId, amount, "TRANSFER OUT: " + description)
                .then(deposit(toId, amount, "TRANSFER IN: " + description))
                .then();
    }

    @Override public Flux<Movement> movements(String accountId) { return movementRepo.findByAccountId(accountId); }
}
