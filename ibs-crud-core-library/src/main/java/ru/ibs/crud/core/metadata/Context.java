package ru.ibs.crud.core.metadata;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Context<DTO, ENTITY> {
    public static final String PARAMS = "PARAMS";
    public static final String RESPONSE = "RESPONSE";
    public static final String CURRENT_DTO = "CURRENT_DTO";
    public static final String CURRENT_ENTITY = "CURRENT_ENTITY";
    public static final String ENTITY_ID = "ENTITY_ID";
    public static final String PRE_ENTITY = "PRE_ENTITY";
    public static final String PRE_DTO = "PRE_DTO";
    public static final String POST_ENTITY = "POST_ENTITY";
    public static final String POST_DTO = "POST_DTO";

    public static final String PRE_DTO_LIST = "PRE_DTO_LIST";
    public static final String POST_DTO_LIST = "POST_DTO_LIST";
    public static final String PRE_ENTITY_LIST = "PRE_ENTITY_LIST";
    public static final String POST_ENTITY_LIST = "POST_ENTITY_LIST";
    public static final String ENTITY_ID_LIST = "ENTITY_ID_LIST";

    private Map<String, Object> objects = new HashMap<>();

    public Context() {
    }

    public Context(DTO preDto, ENTITY preEntity) {
        add(PRE_DTO, preDto);
        add(PRE_ENTITY, preEntity);
    }

    @Deprecated
    public ENTITY getPreEntity() {
        return (ENTITY) get(PRE_ENTITY);
    }

    @Deprecated
    public void setPreEntity(ENTITY preEntity) {
        add(PRE_ENTITY, preEntity);
    }

    @Deprecated
    public DTO getPreDto() {
        return (DTO) get(PRE_DTO);
    }

    @Deprecated
    public void setPreDto(DTO preDto) {
        add(PRE_DTO, preDto);
    }

    @Deprecated
    public ENTITY getPostEntity() {
        return (ENTITY) get(POST_ENTITY);
    }

    @Deprecated
    public void setPostEntity(ENTITY postEntity) {
        add(POST_ENTITY, postEntity);
    }

    @Deprecated
    public DTO getPostDto() {
        return (DTO) get(POST_DTO);
    }

    @Deprecated
    public void setPostDto(DTO postDto) {
        add(POST_DTO, postDto);
    }

    public Context<DTO, ENTITY> add(String key, Object value) {
        objects.put(key, value);
        return this;
    }

    public Object get(String key) {
        return objects.get(key);
    }
}
