package ru.ibs.crud.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseDto<DTO> {
    private List<DTO> content;
    private Long totalElements;
    private boolean success;
    private String msg = "";

    public ResponseDto() {
    }

    public ResponseDto(List<DTO> content, Long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
        success = true;
    }
}