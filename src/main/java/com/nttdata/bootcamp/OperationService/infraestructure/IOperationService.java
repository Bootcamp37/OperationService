package com.nttdata.bootcamp.OperationService.infraestructure;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IOperationService {
    Flux<OperationResponse> getAll();

    Mono<OperationResponse> getById(String id);

    Mono<OperationResponse> save(OperationRequest request);

    Mono<OperationResponse> update(OperationRequest request, String id);

    Mono<OperationResponse> delete(String id);
}
