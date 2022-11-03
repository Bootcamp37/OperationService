package com.nttdata.bootcamp.OperationService.domain;

import com.nttdata.bootcamp.OperationService.domain.dto.CustomerPassiveProductRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.domain.entity.OperationType;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationMapper;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationRepository;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService implements IOperationService {
    @Autowired
    private final IOperationRepository repository;
    @Autowired
    private final IOperationMapper mapper;
    @Autowired
    private final CustomerPassiveProductRepository customerProductRepository;

    @Override
    public Flux<OperationResponse> getAll() {
        log.debug("====> OperationService: getAll");
        return repository.findAll()
                .map(mapper::toResponse);
    }

    @Override
    public Mono<OperationResponse> getById(String id) {
        log.debug("====> OperationService: getAll");
        return repository.findById(id)
                .map(mapper::toResponse)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }

    @Override
    public Mono<OperationResponse> save(Mono<OperationRequest> request) {
        log.debug("====> OperationService: Save");
        // Existe la cuenta?
        return request.map(this::printDebug)
                .flatMap(e -> customerProductRepository.getById(e.getCustomerPassiveProductId())
                .flatMap(customerPassiveProductResponse -> {
                    // Coloca la fecha
                    e.setOperationDate(getDate());
                    // Es retiro y no tiene saldo?
                    if (e.getOperationType().equals(OperationType.WITHDRAWAL) && customerPassiveProductResponse.getAmount() < e.getAmount()) {
                        // Retorna error
                        return Mono.error(RuntimeException::new);
                    }
                    CustomerPassiveProductRequest update = new CustomerPassiveProductRequest();
                    BeanUtils.copyProperties(customerPassiveProductResponse, update);
                    // Contar el numero de transacciones en el mes
                    Integer numTranx = 0; // GetNumeroTransacciones();
                    if(numTranx > customerPassiveProductResponse.getMaxMovementFree()){
                        e.setCommission(customerPassiveProductResponse.getCommission());
                        update.setAmount(update.getAmount() - e.getCommission());
                    }
                    if (e.getOperationType().equals(OperationType.WITHDRAWAL)) {
                        update.setAmount(customerPassiveProductResponse.getAmount() - e.getAmount());
                    }
                    if (e.getOperationType().equals(OperationType.DEPOSIT)) {
                        update.setAmount(customerPassiveProductResponse.getAmount() + e.getAmount());
                    }
                    // Actualizar saldo
                    return customerProductRepository.update(update, e.getCustomerPassiveProductId())
                            // Guardar operacion
                            .flatMap(p -> Mono.just(e))
                            .map(mapper::toEntity)
                            .flatMap(repository::save)
                            .map(mapper::toResponse)
                            .switchIfEmpty(Mono.error(RuntimeException::new));
                })
                // No Existe
                // Mandar error
                .switchIfEmpty(Mono.error(RuntimeException::new)));
    }

    @Override
    public Mono<OperationResponse> update(Mono<OperationRequest> request, String id) {
        log.debug("====> OperationService: Update");
        return Mono.just(new OperationResponse());
    }

    @Override
    public Mono<OperationResponse> delete(String id) {
        log.debug("====> OperationService: Delete");
        return repository.findById(id)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(deleteCustomer -> repository.delete(deleteCustomer)
                        .then(Mono.just(mapper.toResponse(deleteCustomer))));
    }

    public String getDate() {
        log.debug("====> OperationService: getDate");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    public OperationRequest printDebug(OperationRequest request){
        log.debug("====> OperationService: printDebug");
        log.debug("====> OperationService: Request ==> " + request.toString());
        return request;
    }
}
