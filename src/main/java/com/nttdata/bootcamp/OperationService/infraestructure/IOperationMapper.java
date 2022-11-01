package com.nttdata.bootcamp.OperationService.infraestructure;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.domain.entity.Operation;

public interface IOperationMapper {
    Operation toEntity(OperationRequest request);

    OperationResponse toResponse(Operation operation);
}
