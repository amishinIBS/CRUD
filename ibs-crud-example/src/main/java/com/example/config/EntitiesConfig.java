package com.example.config;

import com.example.dto.AnotherExampleDto;
import com.example.dto.ExampleDto;
import com.example.dto.ThirdExampleDto;
import com.example.entities.AnotherExampleEntity;
import com.example.entities.ExampleEntity;
import com.example.entities.ThirdExampleEntity;
import com.example.repo.AnotherExampleRepository;
import com.example.repo.ExampleRepository;
import com.example.repo.ThirdExampleRepository;
import com.example.service.ThirdExampleEntityServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.flk.ValidationResult;
import ru.ibs.crud.core.flk.ValidationService;
import ru.ibs.crud.core.mapper.Mapper;
import ru.ibs.crud.core.metadata.DefaultEntityMetadataProviderImpl;
import ru.ibs.crud.core.metadata.EntityMetadata;
import ru.ibs.crud.core.metadata.EntityMetadataProvider;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.core.services.FindService;
import ru.ibs.crud.core.services.interceptors.*;
import ru.ibs.crud.spring.mapper.SpringAbstractMapperImpl;
import ru.ibs.crud.spring.services.AbstractCrudServiceImpl;
import ru.ibs.crud.spring.services.AbstractFindService;

@Configuration
public class EntitiesConfig {

    @Bean
    public CRUDService<Long, ExampleDto, ValidationCondition<Long, String>> exampleCrudService(ExampleRepository repository,
                                                                                               Mapper<ExampleDto, ExampleEntity> mapper,
                                                                                               @Autowired(required = false)EntityInterceptors<ExampleDto, ExampleEntity> interceptors,
                                                                                               @Autowired(required = false) ValidationService<ExampleEntity, ValidationCondition<Long, String>> validationService,
                                                                                               ObjectMapper objectMapper) {

        return new AbstractCrudServiceImpl<>(repository, mapper, interceptors, validationService, objectMapper){};
    }

    @Bean
    public Mapper<ExampleDto, ExampleEntity> exampleMapper() {
        return new SpringAbstractMapperImpl<>(){};
    }

    @Bean
    public EntityInterceptors<ExampleDto, ExampleEntity> entityInterceptors() {
        return new EntityInterceptors<>(null){};
    }

    @Bean
    public ValidationService<?, ValidationCondition<Long, String>> validationService() {
        return (domainId, target) -> ValidationResult.<ValidationCondition<Long, String>>builder().build();
    }

    @Bean
    public FindService<ExampleDto> exampleFindService(ExampleRepository repository,
                                                      Mapper<ExampleDto, ExampleEntity> mapper, EntityInterceptors<ExampleDto, ExampleEntity> entityInterceptors) {
        return new AbstractFindService<>(repository, mapper, entityInterceptors) {};
    }







    @Bean
    public CRUDService<Long, AnotherExampleDto, ValidationCondition<Long, String>> anotherExampleCrudService(AnotherExampleRepository repository,
                                                                                                             Mapper<AnotherExampleDto, AnotherExampleEntity> mapper,
                                                                                                             @Autowired(required = false)EntityInterceptors<AnotherExampleDto, AnotherExampleEntity> anotherInterceptors,
                                                                                                             @Autowired(required = false) ValidationService<AnotherExampleEntity, ValidationCondition<Long, String>> validationService,
                                                                                                             ObjectMapper objectMapper) {

        return new AbstractCrudServiceImpl<>(repository, mapper, anotherInterceptors, validationService, objectMapper){};
    }

    @Bean
    public Mapper<AnotherExampleDto, AnotherExampleEntity> anotherExampleMapper() {
        return new SpringAbstractMapperImpl<>(){};
    }

    @Bean
    public EntityInterceptors<AnotherExampleDto, AnotherExampleEntity> anotherInterceptors() {
        return new EntityInterceptors<>(null){};
    }

    @Bean
    public FindService<AnotherExampleDto> anotherExampleFindService(AnotherExampleRepository repository,
                                                                    Mapper<AnotherExampleDto, AnotherExampleEntity> mapper,
                                                                    EntityInterceptors<AnotherExampleDto, AnotherExampleEntity> anotherInterceptors) {
        return new AbstractFindService<>(repository, mapper, anotherInterceptors) {};
    }






    @Bean
    public CRUDService<Long, ThirdExampleDto, ValidationCondition<Long, String>> thirdExampleCrudService(ThirdExampleRepository repository,
                                                                                                         Mapper<ThirdExampleDto, ThirdExampleEntity> mapper,
                                                                                                         @Autowired(required = false) EntityInterceptors<ThirdExampleDto, ThirdExampleEntity> thirdEntityInterceptors,
                                                                                                         @Autowired(required = false) ValidationService<ThirdExampleEntity, ValidationCondition<Long, String>> validationService,
                                                                                                         ObjectMapper objectMapper) {

        return new ThirdExampleEntityServiceImpl(repository, mapper, thirdEntityInterceptors, validationService, objectMapper);
    }

    @Bean
    public Mapper<ThirdExampleDto, ThirdExampleEntity> thirdExampleMapper() {
        return new SpringAbstractMapperImpl<>(){};
    }

    @Bean
    public EntityInterceptors<ThirdExampleDto, ThirdExampleEntity> thirdEntityInterceptors() {
        return new EntityInterceptors<>(null){};
    }

    @Bean
    public FindService<ThirdExampleDto> thirdExampleFindService(ThirdExampleRepository repository,
                                                                Mapper<ThirdExampleDto, ThirdExampleEntity> mapper,
                                                                EntityInterceptors<ThirdExampleDto, ThirdExampleEntity> thirdEntityInterceptors) {
        return new AbstractFindService<>(repository, mapper, thirdEntityInterceptors) {};
    }




    @Bean
    public EntityMetadataProvider entityMetadataProvider(ApplicationContext applicationContext) {
        final DefaultEntityMetadataProviderImpl defaultEntityMetadataProvider = new DefaultEntityMetadataProviderImpl();


        final EntityMetadata<Long, ExampleDto, ValidationCondition<Long, String>> entityMetadata
                = new EntityMetadata<>(ExampleEntity.class.getSimpleName(),
                (CRUDService<Long, ExampleDto, ValidationCondition<Long, String>>) applicationContext.getBean("exampleCrudService"),
                (FindService<ExampleDto>) applicationContext.getBean("exampleFindService"));

        final EntityMetadata<Long, AnotherExampleDto, ValidationCondition<Long, String>> anotherEntityMetadata
                = new EntityMetadata<>(AnotherExampleEntity.class.getSimpleName(),
                (CRUDService<Long, AnotherExampleDto, ValidationCondition<Long, String>>) applicationContext.getBean("anotherExampleCrudService"),
                (FindService<AnotherExampleDto>) applicationContext.getBean("anotherExampleFindService"));


        final EntityMetadata<Long, AnotherExampleDto, ValidationCondition<Long, String>> thirdEntityMetadata
                = new EntityMetadata<>(ThirdExampleEntity.class.getSimpleName(),
                (CRUDService<Long, AnotherExampleDto, ValidationCondition<Long, String>>) applicationContext.getBean("thirdExampleCrudService"),
                (FindService<AnotherExampleDto>) applicationContext.getBean("thirdExampleFindService"));

        defaultEntityMetadataProvider.addEntity(entityMetadata)
                                     .addEntity(anotherEntityMetadata)
                                     .addEntity(thirdEntityMetadata);

        return defaultEntityMetadataProvider;
    }


}
