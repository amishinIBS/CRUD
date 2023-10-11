package ru.ibs.crud.tests.testdata.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ibs.crud.tests.testdata.entity.TestEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestDto extends TestEntity {

    public TestDto(Long id, String exampleFieldOne, Long exampleFieldTwo) {
        super(id, exampleFieldOne, exampleFieldTwo);
    }
}