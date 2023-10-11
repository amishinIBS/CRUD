package ru.ibs.crud.spring.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.flk.ValidationContext;
import ru.ibs.crud.core.flk.ValidationResult;
import ru.ibs.crud.core.flk.ValidationService;
import ru.ibs.crud.core.mapper.Mapper;
import ru.ibs.crud.core.metadata.Context;
import ru.ibs.crud.core.metadata.HandlerType;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.core.services.interceptors.EntityInterceptors;
import ru.ibs.crud.spring.exception.NotFoundException;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCrudServiceImpl<TYPE_ID,
        DTO,
        ENTITY,
        REPOSITORY extends JpaRepository<ENTITY, TYPE_ID>,
        MAPPER extends Mapper<DTO, ENTITY>,
        CONDITION extends ValidationCondition<?, ?>>
        implements CRUDService<TYPE_ID, DTO, CONDITION> {

    private final Class<TYPE_ID> typeIdClass;
    private final Class<DTO> dtoClass;
    private final Class<ENTITY> entityClass;


    {
        final Class<?>[] genericClasses = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractCrudServiceImpl.class);
        Assert.notNull(genericClasses, "Can not resolve generic parameters");
        //noinspection unchecked
        this.typeIdClass = (Class<TYPE_ID>) genericClasses[0];
        //noinspection unchecked
        this.dtoClass = (Class<DTO>) genericClasses[1];
        //noinspection unchecked
        this.entityClass = (Class<ENTITY>) genericClasses[2];
    }

    @Getter(AccessLevel.PROTECTED)
    private final REPOSITORY repository;

    @Getter(AccessLevel.PROTECTED)
    private final MAPPER mapper;

    @Getter(AccessLevel.PROTECTED)
    private final EntityInterceptors<DTO, ENTITY> interceptors;

    @Getter(AccessLevel.PROTECTED)
    private final ValidationService<ENTITY, CONDITION> validationService;

    @Getter(AccessLevel.PROTECTED)
    private final ObjectMapper objectMapper;

    public AbstractCrudServiceImpl(REPOSITORY repository,
                                   MAPPER mapper,
                                   EntityInterceptors<DTO, ENTITY> interceptors,
                                   ValidationService<ENTITY, CONDITION> validationService,
                                   ObjectMapper objectMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.interceptors = interceptors;
        this.validationService = validationService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public DTO create(DTO dto) {
        ENTITY newEntity = getMapper().dtoToEntity(dto);
        return create(dto, newEntity);
    }

    @Override
    @Transactional
    public List<DTO> createAll(List<DTO> dtoItems) {
        List<ENTITY> newEntities = dtoItems.stream().map(dto -> getMapper().dtoToEntity(dto)).collect(Collectors.toList());
        newEntities.stream().forEach(this::checkId);
        Context<DTO, ENTITY> context = new Context<>();
        context.add(Context.PRE_DTO_LIST, dtoItems)
                .add(Context.PRE_ENTITY_LIST, newEntities);

        interceptors.preHandle(context, HandlerType.SAVE);
        newEntities = getRepository().saveAll(newEntities);
        List<DTO> newDtos = newEntities.stream().map(entity -> getMapper().entityToDto(entity)).collect(Collectors.toList());

        context.add(Context.POST_ENTITY_LIST, newEntities);
        context.add(Context.POST_DTO_LIST, newDtos);

        interceptors.postHandle(context, HandlerType.SAVE);

        return newDtos;
    }

    @Override
    @Transactional
    public DTO create(DTO dto, ValidationContext<CONDITION> context) {
        ENTITY newEntity = getMapper().dtoToEntity(dto);
        validate(newEntity, context);
        return create(dto, newEntity);
    }

    private DTO create(DTO dto, ENTITY newEntity) {
        checkId(newEntity);
        Context<DTO, ENTITY> context = new Context<>(dto, newEntity);

        interceptors.preHandle(context, HandlerType.SAVE);
        newEntity = getRepository().save(newEntity);
        DTO newDto = getMapper().entityToDto(newEntity);

        context.add(Context.POST_ENTITY, newEntity);
        context.add(Context.POST_DTO, newDto);

        interceptors.postHandle(context, HandlerType.SAVE);

        return newDto;
    }

    private void checkId(ENTITY newEntity) {
        val entityId = typeIdClass.cast(getEntityId(newEntity));
        if (entityId != null && getRepository().existsById(entityId)) {
            throw new IllegalArgumentException("Попытка создать уже существующую запись с указанным id = " + entityId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DTO read(TYPE_ID id) {
        if (isNullId(id)) {
            throw new IllegalArgumentException("Не указан Id");
        }

        TYPE_ID realId = convertIdToTypeId(id);
        Context<DTO, ENTITY> context = new Context<>();

        final ENTITY found = findById(realId);
        DTO newDto = getMapper().entityToDto(found);

        context.add(Context.POST_DTO, newDto)
                .add(Context.POST_ENTITY, found);

        interceptors.postHandle(context, HandlerType.FIND);

        return newDto;
    }

    @Override
    @Transactional
    public DTO update(DTO dto, TYPE_ID id) {
        if (isNullId(id)) {
            throw new IllegalArgumentException("Не указан Id");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Не указан Dto");
        }
        TYPE_ID realId = convertIdToTypeId(id);
        ENTITY curEntity = findById(realId);
        ENTITY newEntity = getMapper().dtoToEntity(dto);
        setEntityId(newEntity, realId);

        return updateInternal(dto, curEntity, newEntity);
    }

    @Override
    @Transactional
    public DTO update(DTO dto, TYPE_ID id, ValidationContext<CONDITION> context) {
        if (isNullId(id)) {
            throw new IllegalArgumentException("Не указан Id");
        }
        ENTITY curEntity = findById(id);
        ENTITY newEntity = getMapper().dtoToEntity(dto);
        validate(newEntity, context);

        return updateInternal(dto, curEntity, newEntity);
    }

    @Override
    @Transactional
    public DTO upsert(DTO dto) {
        ENTITY newEntity = getMapper().dtoToEntity(dto);
        TYPE_ID entityId = typeIdClass.cast(getEntityId(newEntity));
        if (entityId != null && getRepository().existsById(entityId)) {
            return update(dto, entityId);
        }
        return create(dto);
    }

    @Override
    @Transactional
    public void delete(TYPE_ID id) {
        if (isNullId(id)) {
            throw new IllegalArgumentException("Не указан Id");
        }

        TYPE_ID realId = convertIdToTypeId(id);
        DTO dto = read(realId);
        Context<DTO, ENTITY> context = new Context<>(dto, null);

        context.add(Context.ENTITY_ID, realId);
        interceptors.preHandle(context, HandlerType.DELETE);

        getRepository().deleteById(realId);

        interceptors.postHandle(context, HandlerType.DELETE);
    }

    @Override
    @Transactional
    public void deleteAll(List<TYPE_ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("Не указаны Id");
        }

        List<TYPE_ID> realIds = ids.stream().map(id -> convertIdToTypeId(id)).collect(Collectors.toList());
        Context<DTO, ENTITY> context = new Context<>();
        context.add(Context.ENTITY_ID_LIST, realIds);

        interceptors.preHandle(context, HandlerType.DELETE);

        getRepository().deleteAllById(realIds);

        interceptors.postHandle(context, HandlerType.DELETE);
    }


    @Override
    @Transactional
    public void deleteEntities(List<DTO> items) {
        if (CollectionUtils.isEmpty(items)) {
            throw new IllegalArgumentException("Список пуст");
        }

        List<ENTITY> entities = items.stream().map(item -> getMapper().dtoToEntity(item)).collect(Collectors.toList());
        Context<DTO, ENTITY> context = new Context<>();
        context.add(Context.PRE_ENTITY_LIST, entities);

        interceptors.preHandle(context, HandlerType.DELETE);

        getRepository().deleteAll(entities);

        interceptors.postHandle(context, HandlerType.DELETE);
    }


    private boolean isNullId(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue() <= 0;
        }
        if (value instanceof String) {
            return !StringUtils.hasText((String) value);
        }
        return false;
    }

    protected ENTITY findById(TYPE_ID id) {
        final Optional<ENTITY> optional = getRepository().findById(id);
        optional.orElseThrow(() -> new NotFoundException("Запись не найдена по id=" + id));
        return optional.get();
    }

    private DTO updateInternal(DTO dto, ENTITY curEntity, ENTITY newEntity) {
        Context<DTO, ENTITY> context = new Context<>(dto, newEntity);
        DTO currentDto = getMapper().entityToDto(curEntity);
        context.add(Context.CURRENT_ENTITY, curEntity)
                .add(Context.CURRENT_DTO, currentDto);

        interceptors.preHandle(context, HandlerType.SAVE);
        newEntity = getRepository().save(newEntity);
        DTO newDto = getMapper().entityToDto(newEntity);

        context.add(Context.POST_ENTITY, newEntity)
                .add(Context.POST_DTO, newDto);

        interceptors.postHandle(context, HandlerType.SAVE);

        return newDto;
    }

    protected void validate(ENTITY target, ValidationContext<CONDITION> context) {
        if (getValidationService() == null) {
            log.debug("Skipping validation due to validation service is not set");
            return;
        }
        var conditionalResult = getValidationService().validate(context.getDomainId(), target);
        context.setValidationResult(conditionalResult);
        if (isContainCriticalErrors(conditionalResult)) {
            throw getValidationException(conditionalResult);
        }
    }

    protected boolean isContainCriticalErrors(@SuppressWarnings("unused") ValidationResult<CONDITION> validationResult) {
        return false;
    }

    protected RuntimeException getValidationException(ValidationResult<CONDITION> validationResult) {
        final String errorsMessages = validationResult.getResult().stream()
                .map(ValidationCondition::getErrorMessage)
                .map(Object::toString)
                .collect(Collectors.toList()).toString();
        return new RuntimeException(errorsMessages);
    }

    protected TYPE_ID convertIdToTypeId(TYPE_ID id) {
        if (Objects.equals(id.getClass(), typeIdClass)) {
            return id;
        }
        return getObjectMapper().convertValue(id, typeIdClass);
    }

    private Optional<Field> findIdField() {
        return getAllFields(entityClass).stream().filter(f -> f.isAnnotationPresent(Id.class)).findAny();
    }

    private List<Field> getAllFields(Class<?> entityClass) {
        Map<String, Field> fieldMap = Arrays.stream(entityClass.getDeclaredFields()).collect(Collectors.toMap(Field::getName, f -> f));
        if(entityClass.getSuperclass() != null) {
            getAllFields(entityClass.getSuperclass()).stream()
                    .filter(f -> !fieldMap.containsKey(f.getName()))
                    .forEach(f -> fieldMap.put(f.getName(), f));
        }
        return new ArrayList<>(fieldMap.values());
    }

    @SneakyThrows
    private Object getEntityId(ENTITY entity) {
        if (findIdField().isPresent()) {
            val field = findIdField().get();
            field.setAccessible(true);
            return field.get(entity);
        }
        return null;
    }

    @SneakyThrows
    private void setEntityId(ENTITY entity, TYPE_ID id) {
        if (findIdField().isPresent()) {
            val field = findIdField().get();
            field.setAccessible(true);
            field.set(entity, id);
        }
    }

    @Override
    public DTO createDto(Map<String, Object> dto) {
        return objectMapper.convertValue(dto, dtoClass);
    }
}
