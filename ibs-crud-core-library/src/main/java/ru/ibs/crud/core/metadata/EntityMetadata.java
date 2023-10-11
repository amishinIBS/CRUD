package ru.ibs.crud.core.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.core.services.FindService;

@AllArgsConstructor
public class EntityMetadata<TYPE_ID, DTO, CONDITION extends ValidationCondition<?,?>> {
    @Getter
    private final String entityName;
    @Getter
    private final CRUDService<TYPE_ID, DTO, CONDITION> crudService;
    @Getter
    private final FindService<DTO> findService;
}