package com.example.repo;

import com.example.entities.ExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExampleRepository extends JpaRepository<ExampleEntity, Long>,
                                           JpaSpecificationExecutor<ExampleEntity> {
}
