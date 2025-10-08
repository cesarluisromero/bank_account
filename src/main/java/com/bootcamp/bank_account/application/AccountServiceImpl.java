package com.bootcamp.bank_account.application;

import com.bootcamp.bank_account.application.service.AccountRules;
import com.bootcamp.bank_account.domain.model.*;
import com.bootcamp.bank_account.domain.port.in.AccountUseCase;
import com.bootcamp.bank_account.domain.port.out.*;
import com.bootcamp.bank_account.infrastructure.out.client.CustomerClient;
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
    private final CustomerClient customerClient;
    private final AccountRules rules;

    @Override
    public Mono<Account> create(Account input) {
        // 1) Verifica que exista el customer y obtén su tipo
        return customerClient.findByDocument(input.getCustomerId())

                // 2) Aplica reglas de negocio según el tipo de cliente + tipo de cuenta a crear
                .flatMap(cust -> rules.validate(cust, input))

                // 3) Garantiza unicidad de accountNumber
                .then(accountRepo.findByAccountNumber(input.getAccountNumber()))
                .flatMap(a -> Mono.<Account>error(new IllegalStateException("accountNumber already exists")))

                // 4) Si no existe el accountNumber, persiste la nueva cuenta
                .switchIfEmpty(Mono.defer(() ->
                        accountRepo.save(
                                input.toBuilder()
                                        .activeInactive(Active.ACTIVE)
                                        .balance(input.getBalance() == null ? BigDecimal.ZERO : input.getBalance())
                                        .build()
                        )
                ));
    }

    @Override public Mono<Account> findById(String id) {
        return accountRepo.findById(id); }
    @Override public Flux<Account> listByCustomer(String customerId) {
        return accountRepo.findByCustomerId(customerId);
    }

    @Transactional
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
