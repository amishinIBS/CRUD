package ru.ibs.crud.spring.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;
import ru.ibs.crud.core.metadata.EntityMetadata;
import ru.ibs.crud.core.metadata.EntityMetadataProvider;
import ru.ibs.crud.core.services.CRUDService;

import java.util.Map;

@RestController
@ConditionalOnExpression("${ru.ibs.crud.controller.enabled:false}")
@RequestMapping("${ru.ibs.crud.endpoint:crud}" + "/**")
public class DefaultCrudController {

    private final EntityMetadataProvider entityMetadataProvider;

    public DefaultCrudController(EntityMetadataProvider entityMetadataProvider) {
        this.entityMetadataProvider = entityMetadataProvider;
    }

    @GetMapping("{name}/{id}")
    public <TYPE_ID> Object read(@PathVariable String name, @PathVariable TYPE_ID id) {
        @SuppressWarnings("unchecked") final EntityMetadata<TYPE_ID, ?, ?> entityMetadata = (EntityMetadata<TYPE_ID, ?, ?>) entityMetadataProvider.getEntitiesMap().get(name);
        final CRUDService<TYPE_ID, ?, ?> crudService = entityMetadata.getCrudService();
        return crudService.read(id);
    }

    @PostMapping("{name}")
    public <TYPE_ID, DTO> Object upsert(@PathVariable String name, @RequestBody Map<String, Object> dto) {
        @SuppressWarnings("unchecked") final EntityMetadata<TYPE_ID, DTO, ?> entityMetadata = (EntityMetadata<TYPE_ID, DTO, ?>) entityMetadataProvider.getEntitiesMap().get(name);
        final CRUDService<TYPE_ID, DTO, ?> crudService = entityMetadata.getCrudService();
        return crudService.upsert(crudService.createDto(dto));
    }

    @PutMapping("{name}/{id}")
    public <TYPE_ID, DTO> DTO update(@PathVariable String name, @PathVariable TYPE_ID id, @RequestBody Map<String, Object> dto) {
        @SuppressWarnings("unchecked") final EntityMetadata<TYPE_ID, DTO, ?> entityMetadata = (EntityMetadata<TYPE_ID, DTO, ?>) entityMetadataProvider.getEntitiesMap().get(name);
        final CRUDService<TYPE_ID, DTO, ?> crudService = entityMetadata.getCrudService();
        return crudService.update(crudService.createDto(dto), id);
    }

    @DeleteMapping("{name}/{id}")
    public <TYPE_ID> void delete(@PathVariable String name, @PathVariable TYPE_ID id) {
        @SuppressWarnings("unchecked") final EntityMetadata<TYPE_ID, ?, ?> entityMetadata = (EntityMetadata<TYPE_ID, ?, ?>) entityMetadataProvider.getEntitiesMap().get(name);
        final CRUDService<TYPE_ID, ?, ?> crudService = entityMetadata.getCrudService();
        crudService.delete(id);
    }
}