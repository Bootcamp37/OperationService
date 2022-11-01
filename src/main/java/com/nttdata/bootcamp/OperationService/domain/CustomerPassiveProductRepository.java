package com.nttdata.bootcamp.OperationService.domain;

import com.nttdata.bootcamp.OperationService.domain.dto.CustomerPassiveProductRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.CustomerPassiveProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomerPassiveProductRepository {
    public Mono<CustomerPassiveProductResponse> getById(String idCustomerPassiveProduct) {
        String urlProduct = "http://localhost:8082";
        WebClient webClientProduct = WebClient.builder().baseUrl(urlProduct).build();
        return webClientProduct.get()
                .uri("/api/v1/customerPassivesProducts/{id}", idCustomerPassiveProduct)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CustomerPassiveProductResponse.class);
    }

    public Mono<CustomerPassiveProductResponse> update(CustomerPassiveProductRequest request, String id) {
        String urlProduct = "http://localhost:8082";
        WebClient webClientProduct = WebClient.builder().baseUrl(urlProduct).build();
        return webClientProduct.put()
                .uri("/api/v1/customerPassivesProducts/update/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CustomerPassiveProductResponse.class);
    }
}
