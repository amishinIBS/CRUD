package ru.ibs.crud.core.services.interceptors;

import lombok.AccessLevel;
import lombok.Getter;
import ru.ibs.crud.core.metadata.Context;
import ru.ibs.crud.core.metadata.HandlerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EntityInterceptors<DTO, ENTITY> {

    @Getter(AccessLevel.PROTECTED)
    private final Map<HandlerType, HandlerInterceptor<DTO, ENTITY>> interceptorMap;

    public EntityInterceptors(List<HandlerInterceptor<DTO, ENTITY>> interceptors) {
        this.interceptorMap = interceptorMap(interceptors);
    }

    public void preHandle(Context<DTO, ENTITY> context, HandlerType handlerType) {
        if (contains(handlerType)) {
            get(handlerType).preHandle(context);
        }
    }

    public void postHandle(Context<DTO, ENTITY> context, HandlerType handlerType) {
        if (contains(handlerType)) {
            get(handlerType).postHandle(context);
        }
    }

    public boolean contains(HandlerType handlerType) {
        return interceptorMap.containsKey(handlerType);
    }

    public HandlerInterceptor<DTO, ENTITY> get(HandlerType handlerType) {
        return interceptorMap.get(handlerType);
    }

    private Map<HandlerType, HandlerInterceptor<DTO, ENTITY>> interceptorMap(List<HandlerInterceptor<DTO, ENTITY>> interceptors) {
        if (interceptors == null || interceptors.isEmpty()) {
            return new HashMap<>();
        }
        return interceptors.stream().collect(Collectors.toMap(i -> {
            if (i instanceof SaveInterceptor) {
                return HandlerType.SAVE;
            } else if (i instanceof FindInterceptor) {
                return HandlerType.FIND;
            } else if (i instanceof DeleteInterceptor) {
                return HandlerType.DELETE;
            } else {
                return HandlerType.NONE;
            }
        }, i -> i));
    }
}
