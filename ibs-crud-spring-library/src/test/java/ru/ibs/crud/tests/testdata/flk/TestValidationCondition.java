package ru.ibs.crud.tests.testdata.flk;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ibs.crud.core.flk.ValidationCondition;

@Data
@AllArgsConstructor
public class TestValidationCondition implements ValidationCondition<Long, String> {

    private Long errorCode;
    private String errorMessage;
}
