package ru.ibs.crud.core.flk;

import lombok.Getter;

public class DefaultValidationConditionImpl
        implements ValidationCondition<Long, String> {

    @Getter private Long errorCode;
    @Getter private String errorMessage;
}
