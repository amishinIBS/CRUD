package ru.ibs.crud.core.params;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Group {
    private String field;
    private String aggregate;
}
