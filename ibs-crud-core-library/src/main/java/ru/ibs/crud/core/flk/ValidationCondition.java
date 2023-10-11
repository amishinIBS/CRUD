package ru.ibs.crud.core.flk;

public interface ValidationCondition<CODE, MESSAGE> {
    CODE getErrorCode();
    MESSAGE getErrorMessage();
}
