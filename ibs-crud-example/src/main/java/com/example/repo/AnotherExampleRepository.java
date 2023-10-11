package com.example.repo;

import com.example.entities.AnotherExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnotherExampleRepository extends JpaRepository<AnotherExampleEntity, Long>,
                                                  JpaSpecificationExecutor<AnotherExampleEntity> {
}
