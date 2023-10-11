package com.example.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "EXAMPLE", schema = "EXAMPLE")
public class ExampleEntity {

    @Id
    private Long id;

    private String exampleFieldOne;

    private Long exampleFieldTwo;

    private Date dateCreate;

}
