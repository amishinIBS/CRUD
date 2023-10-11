package ru.ibs.crud.core.services.interceptors;

import ru.ibs.crud.core.metadata.Context;

public interface HandlerInterceptor<DTO, ENTITY> {
    void preHandle(Context<DTO, ENTITY> context);
    void postHandle(Context<DTO, ENTITY> context);
}
