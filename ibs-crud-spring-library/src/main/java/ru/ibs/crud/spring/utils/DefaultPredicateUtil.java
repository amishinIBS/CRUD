package ru.ibs.crud.spring.utils;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ValueHandlerFactory;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import ru.ibs.crud.core.params.FilterCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class DefaultPredicateUtil {
    public static Predicate createPredicate(CriteriaBuilder builder, Path path, FilterCriteria criteria) {
        switch (criteria.getOperation()) {
            case BETWEEN:
                Expression lowerBound = builder.literal(criteria.getValue());
                Expression upperBound = builder.literal(criteria.getValue2());
                return builder.between(path, lowerBound, upperBound);
            case MORE:
                return builder.greaterThan(path, getLiteralExpression(builder, path, criteria.getValue()));
            case MORE_EQ:
                return builder.greaterThanOrEqualTo(path, getLiteralExpression(builder, path, criteria.getValue()));
            case LESS:
                return builder.lessThan(path, getLiteralExpression(builder, path, criteria.getValue()));
            case LESS_EQ:
                return builder.lessThanOrEqualTo(path, getLiteralExpression(builder, path, criteria.getValue()));
            case EQ:
                return builder.equal(path, criteria.getValue());
            case NOT_EQ:
                if(criteria.getValue() != null) {
                    return builder.or(builder.notEqual(path, criteria.getValue()), builder.isNull(path));
                } else {
                    return builder.notEqual(path, criteria.getValue());
                }
            case LIKE:
                return builder.like(path, "%" + criteria.getValue().toString() + "%");
            case NOT_LIKE:
                return builder.notLike(path, "%" + criteria.getValue().toString() + "%");
            case ILIKE:
                return builder.like(builder.lower(path), "%" + criteria.getValue().toString().toLowerCase() + "%");
            case NOT_ILIKE:
                return builder.notLike(builder.lower(path), "%" + criteria.getValue().toString().toLowerCase() + "%");
            case STARTS_WITH:
                return builder.like(path, "%" + criteria.getValue().toString());
            case NOT_STARTS_WITH:
                return builder.notLike(path, "%" + criteria.getValue().toString());
            case IN:
                return builder.in(path).value(criteria.getValue());
            case NOT_IN:
                return builder.not(builder.in(path).value(criteria.getValue()));
            case IS_NULL:
                return builder.isNull(path);
            case IS_NOT_NULL:
                return builder.isNotNull(path);
            default:
                throw new IllegalArgumentException("Not supported operation " + criteria.getOperation());
        }
    }

    private static Expression getLiteralExpression(CriteriaBuilder builder, Path path, Object value) {
        if (ValueHandlerFactory.isNumeric(path.getJavaType())) {
            return new LiteralExpression((CriteriaBuilderImpl) builder, ValueHandlerFactory.convert(value, path.getJavaType()));
        } else {
            return new LiteralExpression((CriteriaBuilderImpl) builder, value);
        }
    }
}
