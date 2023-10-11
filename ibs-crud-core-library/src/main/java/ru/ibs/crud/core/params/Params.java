package ru.ibs.crud.core.params;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.ibs.crud.core.serializer.ParamDeserializer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Params implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;
    private Integer offset;
    private Integer limit;

    private List<Sort> sorts;

    @JsonDeserialize(contentUsing= ParamDeserializer.class)
    private List<Filter> filters;

    public Params addFilterItem(Filter filter) {
        if (filters == null) {
            filters = new LinkedList<>();
        }
        filters.add(filter);
        return this;
    }
}
