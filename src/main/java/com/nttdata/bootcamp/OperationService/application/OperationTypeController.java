package com.nttdata.bootcamp.OperationService.application;

import com.nttdata.bootcamp.OperationService.domain.entity.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${message.path-operationType}")
@RefreshScope
public class OperationTypeController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<OperationType> getAll() {
        log.debug("====> OperationTypeController: GetAll");
        return Flux.fromArray(OperationType.values());
    }
}
