package com.nttdata.bootcamp.OperationService.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPassiveProductRequest {
    private String customerId;
    private String productId;
    private Double maintenance;
    private Integer movementLimit;
    private Integer movementDay;
    private Integer movementMonth;
    private Double amount;
}
