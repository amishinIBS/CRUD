package ru.ibs.crud.core.services;

import java.util.function.Supplier;

public interface TransactionHandler {

    <T> T runInTransaction(Supplier<T> supplier);
}
