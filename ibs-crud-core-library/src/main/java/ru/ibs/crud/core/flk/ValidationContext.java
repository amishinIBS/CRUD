package ru.ibs.crud.core.flk;

import lombok.Data;

@Data
public class ValidationContext<CONDITION extends ValidationCondition<?,?>> {

    private Long domainId;
    private ValidationResult<CONDITION> validationResult;

    public ValidationContext(Long domainId) {
        this.domainId = domainId;
    }
}
