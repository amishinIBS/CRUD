package ru.ibs.crud.core.params;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ibs.crud.core.serializer.ParamDeserializer;
import ru.ibs.crud.core.serializer.ToUpperDeserializer;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexFilter implements Filter {
    private static final long serialVersionUID = 1L;

    @JsonDeserialize(using = ToUpperDeserializer.class)
    private String operator;

    @JsonDeserialize(contentUsing= ParamDeserializer.class)
    private List<Filter> filters;

    public ComplexFilter addFilter(Filter filter) {
        if (filters == null) {
            filters = new LinkedList<>();
        }
        filters.add(filter);
        return this;
    }
}
