package ru.ibs.crud.tests.testdata.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.flk.ValidationService;
import ru.ibs.crud.core.mapper.Mapper;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.core.services.FindService;
import ru.ibs.crud.core.services.interceptors.EntityInterceptors;
import ru.ibs.crud.spring.mapper.SpringAbstractMapperImpl;
import ru.ibs.crud.spring.services.AbstractCrudServiceImpl;
import ru.ibs.crud.spring.services.AbstractFindService;
import ru.ibs.crud.tests.testdata.entity.TestEntity;
import ru.ibs.crud.tests.testdata.model.TestDto;
import ru.ibs.crud.tests.testdata.repo.MockRepo;
import ru.ibs.crud.tests.testdata.repo.MockRepoImpl;

@SpringBootConfiguration
@ComponentScan("ru.ibs.crud.tests")
public class TestsConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MockRepo mockRepo() {
        return new MockRepoImpl();
    }

    @Bean
    public EntityInterceptors<TestDto, TestEntity> entityInterceptors() {
        return new EntityInterceptors<>(null) {
        };
    }

    @Bean
    public CRUDService<Long, TestDto, ValidationCondition<Long, String>> crudService(MockRepo repository,
                                                                                     Mapper<TestDto, TestEntity> mapper,
                                                                                     @Autowired(required = false) EntityInterceptors<TestDto, TestEntity> interceptors,
                                                                                     @Autowired(required = false) ValidationService<TestEntity, ValidationCondition<Long, String>> validationService,
                                                                                     ObjectMapper objectMapper) {

        return new AbstractCrudServiceImpl<>(repository, mapper, interceptors, validationService, objectMapper){};
    }

    @Bean
    public FindService<TestDto> findService(MockRepo repository,
                                            Mapper<TestDto, TestEntity> mapper, EntityInterceptors<TestDto, TestEntity> interceptors) {
        return new AbstractFindService<>(repository, mapper, interceptors) {};
    }


    @Bean
    public Mapper<TestDto, TestEntity> mapper() {
        return new SpringAbstractMapperImpl<>(){};
    }
}
