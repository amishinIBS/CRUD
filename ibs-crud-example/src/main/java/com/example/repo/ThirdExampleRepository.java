package com.example.repo;

import com.example.entities.ThirdExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThirdExampleRepository extends JpaRepository<ThirdExampleEntity, Long>,
                                                JpaSpecificationExecutor<ThirdExampleEntity> {
}
