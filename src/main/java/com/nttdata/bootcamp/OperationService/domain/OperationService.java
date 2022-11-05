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
        return request.map(this::printDebug)
            .flatMap(e ->
                // TRAE LA CUENTA DE LA BD
                customerProductRepository.getById(e.getCustomerPassiveProductId())
                    .flatMap(customerPassiveProductResponse ->
                        // REVISA SI TIENE MOVIMIENTOS RESTANTES
                        repository.findAll()
                            .filter(x -> x.getCustomerPassiveProductId().equals(e.getCustomerPassiveProductId()))
                            .count()
                            .flatMap(count -> {
                                // YA NO HAY MOVIMIENTOS
                                if (count.compareTo(Long.valueOf(customerPassiveProductResponse.getMovementLimit())) >= 0) {
                                    return Mono.error(RuntimeException::new);
                                }
                                Double commision = 0.0;
                                // NO ME QUEDAN MOVIMIENTOS LIBRES
                                if (count.compareTo(Long.valueOf(customerPassiveProductResponse.getMaxMovementFree())) >= 0) {
                                    commision = customerPassiveProductResponse.getCommission();
                                }
                                // RETIRNO
                                // NO TENGO SALDO
                                if (e.getOperationType().equals(OperationType.WITHDRAWAL) && customerPassiveProductResponse.getAmount() < (e.getAmount() + commision)) {
                                    // Retorna error
                                    return Mono.error(RuntimeException::new);
                                }
                                // HACER LA TRANSACCION
                                // SETEAR FECHA
                                e.setOperationDate(getDate());
                                CustomerPassiveProductRequest update = new CustomerPassiveProductRequest();
                                BeanUtils.copyProperties(customerPassiveProductResponse, update);
                                update.setCommission(commision);
                                Double amountUpdate = this.updateAmount(customerPassiveProductResponse.getAmount(),commision,e.getOperationType(),e.getAmount());
                                update.setAmount(amountUpdate);

                                // Actualizar saldo
                                return customerProductRepository.update(update, e.getCustomerPassiveProductId())
                                    // Guardar operacion
                                    .flatMap(p -> Mono.just(e))
                                    .map(mapper::toEntity)
                                    .flatMap(repository::save)
                                    .map(mapper::toResponse)
                                    .switchIfEmpty(Mono.error(RuntimeException::new));
                            })
                    )
            );
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

    public OperationRequest printDebug(OperationRequest request) {
        log.debug("====> OperationService: printDebug");
        log.debug("====> OperationService: Request ==> " + request.toString());
        return request;
    }

    public double updateAmount(Double amountUpdate, Double commision, OperationType operationType, Double change){
        if (operationType.equals(OperationType.WITHDRAWAL)) {
            amountUpdate -= change;
        } else if (operationType.equals(OperationType.DEPOSIT)) {
            amountUpdate += change;
        }
        return (amountUpdate - commision);
    }
}
