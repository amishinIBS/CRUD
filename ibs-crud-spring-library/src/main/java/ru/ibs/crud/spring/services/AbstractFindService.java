package ru.ibs.crud.spring.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.ibs.crud.core.dto.ResponseDto;
import ru.ibs.crud.core.mapper.Mapper;
import ru.ibs.crud.core.metadata.Context;
import ru.ibs.crud.core.metadata.HandlerType;
import ru.ibs.crud.core.params.*;
import ru.ibs.crud.core.services.FindService;
import ru.ibs.crud.core.services.interceptors.EntityInterceptors;
import ru.ibs.crud.spring.domain.OffsetRequest;
import ru.ibs.crud.spring.utils.DefaultPredicateUtil;
import ru.ibs.crud.spring.utils.ReflectionUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Accessors(chain = true)
public abstract class AbstractFindService<DTO, ENTITY>
        implements FindService<DTO> {

    @Getter
    private final Class<ENTITY> entityClass;

    {
        final Class<?>[] genericClasses = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractFindService.class);
        this.entityClass = (Class<ENTITY>) genericClasses[1];
    }

    private Map<String, Class> entityFieldMap;

    @Autowired
    private ObjectMapper objectMapper;

    public enum PageBase {ZERO_BASED, ONE_BASED}

    @Getter(AccessLevel.PROTECTED)
    private final JpaSpecificationExecutor<ENTITY> repository;
    @Getter(AccessLevel.PROTECTED)
    private final Mapper<DTO, ENTITY> mapper;
    private final EntityInterceptors<DTO, ENTITY> interceptors;

    @Setter
    private PageBase pageBase = PageBase.ZERO_BASED;

    public AbstractFindService(JpaSpecificationExecutor<ENTITY> repository, Mapper<DTO, ENTITY> mapper,
                               EntityInterceptors<DTO, ENTITY> interceptors) {
        this.repository = repository;
        this.mapper = mapper;
        this.interceptors = interceptors;
    }

    @Override
    @Transactional(readOnly = true)
    public <PARAMS extends Params> ResponseDto<DTO> find(PARAMS params) {
        Context<DTO, ENTITY> context = new Context<>();
        context.add(Context.PARAMS, params);
        interceptors.preHandle(context, HandlerType.FIND);

        Specification<ENTITY> specification = buildSpecification(params);

        ResponseDto<DTO> response;

        if (params != null && (params.getPage() != null || params.getOffset() != null) && params.getLimit() != null) {
            Page<ENTITY> entitiesPage = getRepository().findAll(specification, buildPageable(params));
            response = new ResponseDto<>(
                    entitiesPage.stream()
                            .map(getMapper()::entityToDto)
                            .collect(Collectors.toList()),
                    entitiesPage.getTotalElements());
        } else {

            List<ENTITY> foundEntities = getRepository().findAll(specification, buildSort(params));

            response = new ResponseDto<>(
                    foundEntities.stream()
                            .map(getMapper()::entityToDto)
                            .collect(Collectors.toList()),
                    (long) foundEntities.size());
        }
        context.add(Context.RESPONSE, response);
        interceptors.postHandle(context, HandlerType.FIND);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public <PARAMS extends Params> ResponseDto<DTO> count(PARAMS params) {
        Specification<ENTITY> specification = buildSpecification(params);
        Long count = getRepository().count(specification);

        return new ResponseDto<>(
                null,
                count);
    }

    protected Specification<ENTITY> buildSpecification(Params params) {
        if (params == null || CollectionUtils.isEmpty(params.getFilters())) {
            return null;
        }

        Specification<ENTITY> filterSpecifications = createSpecification(params.getFilters());
        return Specification.where(filterSpecifications);
    }

    private <F extends Filter> Specification<ENTITY> createSpecification(List<F> filters) {
        return new Specification<ENTITY>() {
            @Override
            public Predicate toPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return getPredicate(root, query, builder, filters, Predicate.BooleanOperator.AND);
            }
        };
    }

    protected <F extends Filter> Predicate getPredicate(Root<ENTITY> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<F> filters, Predicate.BooleanOperator operator) {
        List<Predicate> predicates = filters.stream()
                .map(filter -> {
                    if(filter instanceof FilterItem) {
                        return createPredicate(root, builder, (FilterItem) filter);
                    } else {
                        ComplexFilter complexFilter = (ComplexFilter) filter;
                        return getPredicate(root, query, builder, complexFilter.getFilters(), Predicate.BooleanOperator.valueOf(complexFilter.getOperator()));
                    }
                })
                .collect(Collectors.toList());

        return addPredicates(predicates, builder, operator);
    }

    protected Predicate addPredicates(List<Predicate> predicates, CriteriaBuilder builder, Predicate.BooleanOperator operator) {
        if (predicates.size() == 1) {
            return predicates.get(0);
        } else if (predicates.size() > 1) {
            return operator == Predicate.BooleanOperator.AND ?
                    builder.and(predicates.toArray(new Predicate[0])) : builder.or(predicates.toArray(new Predicate[0]));
        } else {
            throw new IllegalStateException("No criteria specified");
        }
    }

    public Predicate createPredicate(Root<ENTITY> root, CriteriaBuilder builder, FilterItem filterItem) {
        Map<String, Class> fieldMap = getEntityFieldMap();
        Class type = fieldMap.get(filterItem.getProperty());

        return createPredicate(root, builder, filterItem, type);
    }

    public Predicate createPredicate(Root<ENTITY> root, CriteriaBuilder builder, FilterItem filterItem, Class type) {
        Object value1, value2;
        if(filterItem.getValue() instanceof Collection<?>) {
            value1 = objectMapper.convertValue(filterItem.getValue(),
                    objectMapper.getTypeFactory().constructCollectionType(Collection.class, type));
            value2 = objectMapper.convertValue(filterItem.getValue2(),
                    objectMapper.getTypeFactory().constructCollectionType(Collection.class, type));
        }else {
            value1 = objectMapper.convertValue(filterItem.getValue(), type);
            value2 = objectMapper.convertValue(filterItem.getValue2(), type);
        }
        Operator operator = Operator.valueOf(filterItem.getOperator().toUpperCase());
        FilterCriteria filterCriteria = new FilterCriteria(filterItem.getProperty(), operator, value1, value2);
        return DefaultPredicateUtil.createPredicate(builder, root.get(filterItem.getProperty()), filterCriteria);
    }

    private Map<String, Class> getEntityFieldMap() {
        if(entityFieldMap != null && !entityFieldMap.isEmpty()) {
            return entityFieldMap;
        }
        entityFieldMap = ReflectionUtil.getEntityFieldMap(entityClass, entityClass, null);
        return entityFieldMap;
    }

    private Pageable buildPageable(Params params) {
        if(params.getPage() != null) {
            int pageNum = PageBase.ZERO_BASED.equals(pageBase) ? params.getPage() : params.getPage() - 1;
            return PageRequest.of(pageNum, params.getLimit(), buildSort(params));
        } else {
            return OffsetRequest.of(params.getOffset(), params.getLimit(), buildSort(params));
        }
    }

    private Sort buildSort(Params params) {
        if (params == null || CollectionUtils.isEmpty(params.getSorts())) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = params.getSorts().stream()
                .map(sort -> new Sort.Order(Sort.Direction.fromString(sort.getType()), sort.getField()))
                .collect(Collectors.toList());
        return Sort.by(orders);
    }
}
