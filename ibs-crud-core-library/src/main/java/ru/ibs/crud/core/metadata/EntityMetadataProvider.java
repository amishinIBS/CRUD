package ru.ibs.crud.core.metadata;

import java.util.Map;

public interface EntityMetadataProvider {
    Map<String, EntityMetadata<?,?,?>> getEntitiesMap();
}
