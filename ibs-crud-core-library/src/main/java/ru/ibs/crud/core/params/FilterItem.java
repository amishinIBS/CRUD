package ru.ibs.crud.core.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FilterItem implements Filter {
    private static final long serialVersionUID = 1L;

    private String property;
    private String operator;
    private Object value;
    private Object value2;
}