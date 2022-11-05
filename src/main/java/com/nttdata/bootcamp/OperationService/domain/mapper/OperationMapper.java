package com.nttdata.bootcamp.OperationService.domain.mapper;

import com.nttdata.bootcamp.OperationService.domain.dto.OperationRequest;
import com.nttdata.bootcamp.OperationService.domain.dto.OperationResponse;
import com.nttdata.bootcamp.OperationService.domain.entity.Operation;
import com.nttdata.bootcamp.OperationService.infraestructure.IOperationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OperationMapper implements IOperationMapper {
    @Override
    public Operation toEntity(OperationRequest request) {
        log.debug("====> OperationMapper: ToEntity");
        Operation operation = new Operation();
        BeanUtils.copyProperties(request, operation);
        return operation;
    }

    @Override
    public OperationResponse toResponse(Operation operation) {
        log.debug("====> OperationMapper: ToResponse");
        OperationResponse operationResponse = new OperationResponse();
        BeanUtils.copyProperties(operation, operationResponse);
        return operationResponse;
    }
}
