package com.nttdata.bootcamp.OperationService.infraestructure;

import com.nttdata.bootcamp.OperationService.domain.entity.Operation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOperationRepository extends ReactiveMongoRepository<Operation, String> {
}
