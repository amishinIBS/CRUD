package ru.ibs.crud.core.metadata;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEntityMetadataProviderImpl
        implements EntityMetadataProvider {

    @Getter
    Map<String, EntityMetadata<?, ?, ?>> entitiesMap = new ConcurrentHashMap<>();

    public DefaultEntityMetadataProviderImpl addEntity(EntityMetadata<?, ?, ?> entityMetadata) {
        getEntitiesMap().put(entityMetadata.getEntityName(), entityMetadata);
        return this;
    }
}
