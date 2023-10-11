package com.example.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ANOTHER_EXAMPLE", schema = "EXAMPLE")
public class ThirdExampleEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String anotherExampleFieldUno;

    private Long anotherExampleFieldDuo;
}
