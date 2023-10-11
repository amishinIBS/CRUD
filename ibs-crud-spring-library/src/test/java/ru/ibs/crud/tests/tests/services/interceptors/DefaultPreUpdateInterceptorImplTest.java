package ru.ibs.crud.tests.tests.services.interceptors;

import org.junit.jupiter.api.Test;
import ru.ibs.crud.core.metadata.Context;
import ru.ibs.crud.core.metadata.HandlerType;
import ru.ibs.crud.core.services.interceptors.EntityInterceptors;
import ru.ibs.crud.core.services.interceptors.HandlerInterceptor;
import ru.ibs.crud.core.services.interceptors.SaveInterceptor;
import ru.ibs.crud.tests.testdata.entity.TestEntity;
import ru.ibs.crud.tests.testdata.model.TestDto;

import java.util.ArrayList;
import java.util.List;

class DefaultPreUpdateInterceptorImplTest {

    @Test
    void preUpdateTest() {
        TestEntity testEntity = new TestEntity(1L, "TestData", 100000L);
        TestDto testDto = new TestDto(1L, "TestData", 100000L);

        HandlerInterceptor<TestDto, TestEntity> interceptor = createInterceptor();
        List<HandlerInterceptor<TestDto, TestEntity>> interceptors = new ArrayList<>();
        interceptors.add(interceptor);

        EntityInterceptors<TestDto, TestEntity> pui = new EntityInterceptors<>(interceptors){};

        Context<TestDto, TestEntity> context = new Context<>(testDto, testEntity);
        pui.preHandle(context, HandlerType.SAVE);
    }

    private SaveInterceptor<TestDto, TestEntity> createInterceptor() {
        return new SaveInterceptor<>() {

            @Override
            public void preHandle(Context<TestDto, TestEntity> context) {

            }

            @Override
            public void postHandle(Context<TestDto, TestEntity> context) {

            }
        };
    }

}
