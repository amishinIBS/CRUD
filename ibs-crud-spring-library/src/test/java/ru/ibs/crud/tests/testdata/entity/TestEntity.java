package ru.ibs.crud.tests.testdata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id @GeneratedValue
    private Long id;
    private String exampleFieldOne;
    private Long exampleFieldTwo;
}