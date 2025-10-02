package com.bootcamp.bank_account.infrastructure.in.web.controller;

import com.bootcamp.bank_account.domain.model.Account;
import com.bootcamp.bank_account.domain.model.Movement;
import com.bootcamp.bank_account.domain.port.in.AccountUseCase;
import com.bootcamp.bank_account.infrastructure.in.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase useCase;

    @PostMapping
    public Mono<Account> create(@Valid @RequestBody AccountDto dto) {
        return useCase.create(Account.builder()
                .customerId(dto.customerId())
                .type(dto.type())
                .currency(dto.currency())
                .accountNumber(dto.accountNumber())
                .balance(dto.balance())
                .active(true)
                .build());
    }

    @GetMapping("/{id}")
    public Mono<Account> get(@PathVariable String id) { return useCase.findById(id); }

    @GetMapping
    public Flux<Account> byCustomer(@RequestParam String customerId) { return useCase.listByCustomer(customerId); }

    @PatchMapping("/{id}/deposit")
    public Mono<Account> deposit(@PathVariable String id, @Valid @RequestBody AmountDto body) {
        return useCase.deposit(id, body.amount(), body.description());
    }

    @PatchMapping("/{id}/withdraw")
    public Mono<Account> withdraw(@PathVariable String id, @Valid @RequestBody AmountDto body) {
        return useCase.withdraw(id, body.amount(), body.description());
    }

    @PostMapping("/transfer")
    public Mono<Void> transfer(@RequestParam String fromId, @RequestParam String toId,
                               @RequestParam BigDecimal amount, @RequestParam(required=false) String description) {
        return useCase.transfer(fromId, toId, amount, description == null ? "" : description);
    }

    @GetMapping("/{id}/movements")
    public Flux<Movement> movements(@PathVariable String id) { return useCase.movements(id); }
}
