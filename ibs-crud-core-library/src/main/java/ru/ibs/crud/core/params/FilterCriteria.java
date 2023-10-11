package ru.ibs.crud.core.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class FilterCriteria {
    private String key;
    private Operator operation;
    private Object value;
    private Object value2;
}
