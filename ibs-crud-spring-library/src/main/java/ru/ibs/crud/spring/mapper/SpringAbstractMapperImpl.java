package ru.ibs.crud.spring.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import ru.ibs.crud.core.mapper.AbstractMapperImpl;
import ru.ibs.crud.core.mapper.Mapper;

public class SpringAbstractMapperImpl<DTO, ENTITY>
        extends AbstractMapperImpl<DTO, ENTITY>
        implements Mapper<DTO, ENTITY> {

    @Autowired
    @Getter(AccessLevel.PROTECTED)
    private ObjectMapper objectMapper;

    public SpringAbstractMapperImpl() {
        super(null, null);
        {
            final Class<?>[] genericClasses = GenericTypeResolver.resolveTypeArguments(getClass(), Mapper.class);
            if (genericClasses != null) {
                //noinspection unchecked
                setDtoClass((Class<DTO>) genericClasses[0]);
                //noinspection unchecked
                setEntityClass((Class<ENTITY>) genericClasses[1]);
            }
        }
    }

    protected <SOURCE, DESTINATION> DESTINATION map(SOURCE source, Class<DESTINATION> destinationClass) {
        return objectMapper.convertValue(source, destinationClass);
    }

}
