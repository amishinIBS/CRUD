package ru.ibs.crud.core.mapper;

public interface Mapper<DTO, ENTITY> {

    ENTITY dtoToEntity(DTO source);

    DTO entityToDto(ENTITY source);

}