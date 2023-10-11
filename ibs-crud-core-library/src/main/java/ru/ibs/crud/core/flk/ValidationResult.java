package ru.ibs.crud.core.flk;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ValidationResult<CONDITION extends ValidationCondition<?,?>> {
    private final List<CONDITION> result;
}
