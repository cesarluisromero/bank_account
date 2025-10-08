package com.bootcamp.bank_account.infrastructure.out.client;

import com.bootcamp.bank_account.infrastructure.in.web.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

// infrastructure/out/client/CustomerClient.java
@Component
@RequiredArgsConstructor
public class CustomerClient {
    private final WebClient.Builder lb;

    @Value("${customer-service.base-url:http://customer/customers}")
    private String baseUrl;

    public Mono<CustomerDto> findByDocument(String number) {
        return lb.build()
                .get().uri(baseUrl + "/doc/{number}", number)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> Mono.error(new IllegalStateException("customer doc not found: " + number)))
                .bodyToMono(CustomerDto.class);
    }
}
