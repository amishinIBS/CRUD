package ru.ibs.crud.core.services;

import ru.ibs.crud.core.dto.ResponseDto;
import ru.ibs.crud.core.params.Params;

public interface FindService<DTO> {
    <PARAMS extends Params> ResponseDto<DTO> find(PARAMS params);
    <PARAMS extends Params> ResponseDto<DTO> count(PARAMS params);
}