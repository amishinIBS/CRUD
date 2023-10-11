package ru.ibs.crud.spring.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;
import ru.ibs.crud.core.dto.ResponseDto;
import ru.ibs.crud.core.metadata.EntityMetadata;
import ru.ibs.crud.core.metadata.EntityMetadataProvider;
import ru.ibs.crud.core.params.Params;
import ru.ibs.crud.core.services.FindService;

@RestController
@ConditionalOnExpression("${ru.ibs.find.controller.enabled:false}")
@RequestMapping("${ru.ibs.find.endpoint:find}" + "/**")
public class DefaultFindController {

    private final EntityMetadataProvider entityMetadataProvider;

    public DefaultFindController(EntityMetadataProvider entityMetadataProvider) {
        this.entityMetadataProvider = entityMetadataProvider;
    }

    @GetMapping("{name}")
    public ResponseDto<?> findGet(@PathVariable String name, Params params) {
        return find(name, params);
    }

    @PostMapping("{name}")
    public ResponseDto<?> findPost(@PathVariable String name,  @RequestBody Params params) {
        return find(name, params);
    }

    @PostMapping("{name}/count")
    public ResponseDto<?> findCountPost(@PathVariable String name,  @RequestBody Params params) {
        final EntityMetadata<?, ?, ?> entityMetadata = entityMetadataProvider.getEntitiesMap().get(name);
        final FindService<?> findService = entityMetadata.getFindService();
        return findService.count(params);
    }

    private ResponseDto<?> find(String name, Params params) {
        final EntityMetadata<?, ?, ?> entityMetadata = entityMetadataProvider.getEntitiesMap().get(name);
        final FindService<?> findService = entityMetadata.getFindService();
        return findService.find(params);
    }
}
