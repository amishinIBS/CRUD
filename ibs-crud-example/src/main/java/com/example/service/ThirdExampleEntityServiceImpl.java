package com.example.service;

import com.example.dto.ThirdExampleDto;
import com.example.entities.ThirdExampleEntity;
import com.example.repo.ThirdExampleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.flk.ValidationService;
import ru.ibs.crud.core.mapper.Mapper;
import ru.ibs.crud.core.services.interceptors.EntityInterceptors;
import ru.ibs.crud.spring.services.AbstractCrudServiceImpl;
import ru.ibs.crud.core.services.CRUDService;

public class ThirdExampleEntityServiceImpl
        extends AbstractCrudServiceImpl<Long,
                                        ThirdExampleDto,
                                        ThirdExampleEntity,
                                        ThirdExampleRepository,
                                        Mapper<ThirdExampleDto, ThirdExampleEntity>,
                ValidationCondition<Long, String>>
        implements CRUDService<Long, ThirdExampleDto, ValidationCondition<Long, String>> {


    public ThirdExampleEntityServiceImpl(ThirdExampleRepository repository,
                                         Mapper<ThirdExampleDto, ThirdExampleEntity> mapper,
                                         EntityInterceptors<ThirdExampleDto, ThirdExampleEntity> interceptors,
                                         ValidationService<ThirdExampleEntity, ValidationCondition<Long, String>> validationService,
                                         ObjectMapper objectMapper) {
        super(repository, mapper, interceptors, validationService, objectMapper);
    }
}
