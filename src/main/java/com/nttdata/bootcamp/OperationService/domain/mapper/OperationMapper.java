package com.nttdata.bootcamp.OperationService.domain.mapper;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.domain.entity.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OperationMapper implements IOperationMapper {
    @Override
    public Operation toEntity(OperationRequest request) {
        Operation operation = new Operation();
        operation.setCustomerPassiveProductId(request.getCustomerPassiveProductId());
        operation.setOperationType(request.getOperationType());
        operation.setAmount(request.getAmount());
        operation.setOperationDate(request.getOperationDate());
        return operation;
    }

    @Override
    public OperationResponse toResponse(Operation operation) {
        OperationResponse operationResponse = new OperationResponse();
        operationResponse.setId(operation.getId());
        operationResponse.setCustomerPassiveProductId(operation.getCustomerPassiveProductId());
        operationResponse.setOperationType(operation.getOperationType());
        operationResponse.setAmount(operation.getAmount());
        operationResponse.setOperationDate(operation.getOperationDate());
        return operationResponse;
    }
}
