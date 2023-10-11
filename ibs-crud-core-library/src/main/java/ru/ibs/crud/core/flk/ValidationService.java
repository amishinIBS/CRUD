package ru.ibs.crud.core.flk;

public interface ValidationService<ENTITY, CONDITION extends ValidationCondition<?,?>> {

    ValidationResult<CONDITION> validate(Long domainId, ENTITY target);
}
