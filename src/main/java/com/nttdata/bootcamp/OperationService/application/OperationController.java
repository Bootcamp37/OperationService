package com.nttdata.bootcamp.OperationService.application;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("${message.path-operation}")
public class OperationController {
    @Autowired
    private IOperationService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OperationResponse> getAll() {
        return service.getAll();
    }

    @GetMapping(path = "/{id}")
    public Mono<OperationResponse> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Mono<OperationResponse> save(@RequestBody OperationRequest request) {
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    public Mono<OperationResponse> update(@RequestBody OperationRequest request, @PathVariable String id) {
        return service.update(request, id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<OperationResponse> delete(@PathVariable String id) {
        return service.delete(id);
    }
}
