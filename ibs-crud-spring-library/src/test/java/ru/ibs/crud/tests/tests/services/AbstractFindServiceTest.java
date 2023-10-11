package ru.ibs.crud.tests.tests.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ibs.crud.core.dto.ResponseDto;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.params.Params;
import ru.ibs.crud.core.params.Sort;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.core.services.FindService;
import ru.ibs.crud.tests.testdata.config.TestsConfiguration;
import ru.ibs.crud.tests.testdata.entity.TestEntity;
import ru.ibs.crud.tests.testdata.model.TestDto;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestsConfiguration.class)
public class AbstractFindServiceTest {
    @Autowired
    private CRUDService<Long, TestDto, ValidationCondition<Long, String>> crudService;

    @Autowired
    private FindService<TestDto> findService;

    @Autowired
    public JpaRepository<TestEntity, Long> repo;

    @Before
    public void dataFill(){
        repo.deleteAll();
        crudService.create(new TestDto(null, "TestData-One", 100000L));
        crudService.create(new TestDto(null, "TestData-Two", 200000L));
        crudService.create(new TestDto(null, "TestData-Three", 300000L));
        crudService.create(new TestDto(null, "TestData-Four", 400000L));
        crudService.create(new TestDto(null, "TestData-Five", 500000L));
        crudService.create(new TestDto(null, "TestData-Six", 555555L));
        crudService.create(new TestDto(null, "TestData-Seven", 123123L));
        crudService.create(new TestDto(null, "TestData-Eight", 444444L));
    }

    @Test
    public void find(){
        Params params = new Params();
        ResponseDto<TestDto> allFindedDto = findService.find(params);
        Assertions.assertNotNull(allFindedDto.getContent());
        Assertions.assertEquals(8L, allFindedDto.getTotalElements());
        Assertions.assertEquals(3L, allFindedDto.getContent().get(2).getId());
        Assertions.assertEquals("TestData-Three", allFindedDto.getContent().get(2).getExampleFieldOne());
        Assertions.assertEquals(300000L, allFindedDto.getContent().get(2).getExampleFieldTwo());
    }

    @Test
    public void findSort(){
        Params params = new Params().setSorts(List.of(new Sort().setField("exampleFieldOne").setType("ASC")));
        ResponseDto<TestDto> allFindedDto = findService.find(params);
        Assertions.assertEquals(8L, allFindedDto.getTotalElements());
        Assertions.assertEquals("TestData-One", allFindedDto.getContent().get(0).getExampleFieldOne());
    }

    @Test
    public void findPageable(){
        Params params = new Params().setPage(0).setLimit(1);
        ResponseDto<TestDto> allFindedDto = findService.find(params);
        Assertions.assertEquals(8L, allFindedDto.getTotalElements());
        Assertions.assertEquals("TestData-One", allFindedDto.getContent().get(0).getExampleFieldOne());
        Assertions.assertEquals(100000L, allFindedDto.getContent().get(0).getExampleFieldTwo());
    }
}