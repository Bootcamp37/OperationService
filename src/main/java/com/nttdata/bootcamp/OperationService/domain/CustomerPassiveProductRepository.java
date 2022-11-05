package com.nttdata.bootcamp.OperationService.domain;

import com.nttdata.bootcamp.OperationService.domain.dto.CustomerPassiveProductRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.CustomerPassiveProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerPassiveProductRepository {
    public static final String CUSTOMER_PRODUCT_SERVICE = "ms-customerProduct";
    @Value("${message.path-customerProductDomain}")
    public String urlCustomerProduct;
    @Value("${message.path-get}")
    public String pathGet;
    @Value("${message.path-update}")
    public String pathUpdate;
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    public Mono<CustomerPassiveProductResponse> getById(String idCustomerPassiveProduct) {
        log.debug("====> CustomerPassiveProductRepository: GetById");
        WebClient webClientProduct = WebClient.builder().baseUrl(urlCustomerProduct).build();
        return webClientProduct.get()
                .uri(pathGet + "{id}", idCustomerPassiveProduct)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                .bodyToMono(CustomerPassiveProductResponse.class)
                .transform(it -> reactiveCircuitBreakerFactory.create(CUSTOMER_PRODUCT_SERVICE)
                        .run(it, throwable -> Mono.just(new CustomerPassiveProductResponse()))
                );
    }

    public Mono<CustomerPassiveProductResponse> update(CustomerPassiveProductRequest request, String id) {
        log.debug("====> CustomerPassiveProductRepository: Update");
        WebClient webClientProduct = WebClient.builder().baseUrl(urlCustomerProduct).build();
        return webClientProduct.put()
                .uri(pathUpdate + "{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new Exception("Error 400")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Exception("Error 500")))
                .bodyToMono(CustomerPassiveProductResponse.class)
                .transform(it -> reactiveCircuitBreakerFactory.create(CUSTOMER_PRODUCT_SERVICE)
                        .run(it, throwable -> Mono.just(new CustomerPassiveProductResponse()))
                );
    }
}
