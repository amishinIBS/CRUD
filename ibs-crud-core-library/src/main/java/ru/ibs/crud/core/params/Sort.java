package ru.ibs.crud.core.params;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Sort {
    private String field;
    private String type;
}
