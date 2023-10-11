package ru.ibs.crud.core.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractMapperImpl<DTO, ENTITY> implements Mapper<DTO, ENTITY> {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private Class<DTO> dtoClass;
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED) private Class<ENTITY> entityClass;

    public AbstractMapperImpl(Class<DTO> dtoClass, Class<ENTITY> entityClass) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    @Override
    public ENTITY dtoToEntity(DTO source) {
        if (Objects.equals(entityClass, dtoClass)) {
            //noinspection unchecked
            return (ENTITY) source;
        }

        return map(source, entityClass);
    }

    public DTO entityToDto(ENTITY source) {
        if (Objects.equals(dtoClass, entityClass)) {
            //noinspection unchecked
            return (DTO) source;
        }
        return map(source, dtoClass);
    }

    @SneakyThrows
    protected <SOURCE, DESTINATION> DESTINATION map(SOURCE source, Class<DESTINATION> destinationClass) {
        final Map<String, Method> sourceGetterMethods = getAllMethods(source.getClass()).stream()
                                                        .filter(method -> method.getParameterCount() == 0
                                                                && method.getName().length() > 3
                                                                && method.getName().startsWith("get"))
                                                        .collect(Collectors.toMap(Method::getName, method -> method));
        final Map<String, Method> destinationSetterMethods = getAllMethods(destinationClass).stream()
                                                             .filter(method -> method.getParameterCount() == 1
                                                                     && method.getName().length() > 3
                                                                     && method.getName().startsWith("set"))
                                                             .filter(method -> {
                                                                 final String getterName = "get" + method.getName().substring(3);
                                                                 return sourceGetterMethods.containsKey(getterName)
                                                                         && Objects.equals(method.getParameterTypes()[0], sourceGetterMethods.get(getterName).getReturnType());
                                                             })
                                                             .collect(Collectors.toMap(Method::getName, method -> method));

        final DESTINATION destination = destinationClass.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Method> entry : destinationSetterMethods.entrySet()) {
            String destinationName = entry.getKey();
            Method destinationMethod = entry.getValue();
            String sourceName = "get" + destinationName.substring(3);
            Method sourceMethod = sourceGetterMethods.get(sourceName);

            final Object sourceValue = sourceMethod.invoke(source);
            destinationMethod.invoke(destination, sourceValue);
        }

        return destination;
    }

    private List<Method> getAllMethods(Class<?> entityClass) {
        Map<String, List<Method>> fieldMap = Arrays.stream(entityClass.getMethods()).collect(Collectors.groupingBy(Method::getName));
        if(entityClass.getSuperclass() != null) {
            getAllMethods(entityClass.getSuperclass()).stream()
                    .filter(f -> !fieldMap.containsKey(f.getName()))
                    .forEach(f -> {
                        List<Method> methods = new ArrayList<>();
                        methods.add(f);
                        fieldMap.put(f.getName(), methods);
                    });
        }
        return fieldMap.entrySet().stream().flatMap(f -> f.getValue().stream()).collect(Collectors.toList());
    }
}
