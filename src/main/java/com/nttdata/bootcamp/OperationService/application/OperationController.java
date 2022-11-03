package com.nttdata.bootcamp.OperationService.application;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("${message.path-operation}")
@RefreshScope
@Slf4j
public class OperationController {
    @Autowired
    private IOperationService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Flux<OperationResponse> getAll() {
        log.debug("====> OperationController: GetAll");
        return service.getAll();
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Mono<OperationResponse>> getById(@PathVariable String id) {
        log.debug("====> OperationController: GetById");
        Mono<OperationResponse> operationResponseMono = service.getById(id);
        return new ResponseEntity<>(operationResponseMono, operationResponseMono != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> save(@RequestBody OperationRequest request) {
        log.debug("====> OperationController: Save");
        return service.save(Mono.just(request));
    }

    @PutMapping("/update/{id}")
    public Mono<OperationResponse> update(@RequestBody OperationRequest request, @PathVariable String id) {
        log.debug("====> OperationController: Update");
        return service.update(Mono.just(request), id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        log.debug("====> OperationController: Delete");
        return service.delete(id)
                .map(r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
