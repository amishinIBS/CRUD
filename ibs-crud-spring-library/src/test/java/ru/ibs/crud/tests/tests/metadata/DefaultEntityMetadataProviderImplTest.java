package ru.ibs.crud.tests.tests.metadata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ibs.crud.core.metadata.DefaultEntityMetadataProviderImpl;
import ru.ibs.crud.core.metadata.EntityMetadata;
import ru.ibs.crud.core.metadata.EntityMetadataProvider;
import ru.ibs.crud.tests.testdata.flk.TestValidationCondition;
import ru.ibs.crud.tests.testdata.model.TestDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class DefaultEntityMetadataProviderImplTest  {

    Map<String, EntityMetadata<Long, TestDto, TestValidationCondition>> entitiesMap;

    @BeforeEach
    public void before() {
        entitiesMap = new ConcurrentHashMap<>();
    }

    @Test
    void addEntityTest() {
        TestDto testDto = new TestDto(1L, "TestData", 100000L);
        final EntityMetadata<Long, TestDto, TestValidationCondition> metadata = new EntityMetadata<>("testDto", null, null);
        DefaultEntityMetadataProviderImpl dempi = new DefaultEntityMetadataProviderImpl();
        dempi.addEntity(metadata);

        EntityMetadataProvider entityMetadataProvider = dempi;

        Assertions.assertNotNull(entityMetadataProvider.getEntitiesMap());
        Assertions.assertEquals("testDto", entityMetadataProvider.getEntitiesMap().get("testDto").getEntityName());
        Assertions.assertTrue(entityMetadataProvider.getEntitiesMap().containsKey("testDto"));
        Assertions.assertEquals("testDto", entityMetadataProvider.getEntitiesMap().get("testDto").getEntityName());
    }
}