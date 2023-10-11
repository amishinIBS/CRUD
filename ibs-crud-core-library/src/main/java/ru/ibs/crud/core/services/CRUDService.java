package ru.ibs.crud.core.services;

import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.flk.ValidationContext;

import java.util.List;
import java.util.Map;

public interface CRUDService<TYPE_ID, DTO, CONDITION extends ValidationCondition<?,?>> {
    DTO read(TYPE_ID id);

    void delete(TYPE_ID id);

    void deleteAll(List<TYPE_ID> ids);

    void deleteEntities(List<DTO> items);

    DTO createDto(Map<String, Object> dto);

    DTO create(DTO dto);

    List<DTO> createAll(List<DTO> dtoItems);

    DTO create(DTO dto, ValidationContext<CONDITION> context);

    DTO update(DTO dto, TYPE_ID id);

    DTO update(DTO dto, TYPE_ID id, ValidationContext<CONDITION> context);

    DTO upsert(DTO dto);
}
