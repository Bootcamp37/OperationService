package com.nttdata.bootcamp.OperationService.domain.dto;

import com.nttdata.bootcamp.OperationService.domain.entity.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationRequest {
    private String customerPassiveProductId;
    private OperationType operationType;
    private Double amount;
    private String operationDate;
}
