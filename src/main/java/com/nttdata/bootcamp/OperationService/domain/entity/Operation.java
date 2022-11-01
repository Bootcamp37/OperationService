package com.nttdata.bootcamp.OperationService.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "operations")
public class Operation {
    @Id
    private String id;
    private String customerPassiveProductId;
    private OperationType operationType;
    private Double amount;
    private String operationDate;
}
