package ru.ibs.crud.tests.tests.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ibs.crud.core.flk.ValidationCondition;
import ru.ibs.crud.core.services.CRUDService;
import ru.ibs.crud.tests.testdata.config.TestsConfiguration;
import ru.ibs.crud.tests.testdata.entity.TestEntity;
import ru.ibs.crud.tests.testdata.model.TestDto;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestsConfiguration.class)
public class AbstractCrudServiceImplTest {

    @Autowired
    private CRUDService<Long, TestDto, ValidationCondition<Long, String>> crudService;

    @Autowired
    public JpaRepository<TestEntity, Long> repo;

    @Before
    public void dataFill(){
        repo.deleteAll();
        crudService.create(new TestDto(null, "TestData-1", 1000000L));
        crudService.create(new TestDto(null, "TestData-2", 2000000L));
        crudService.create(new TestDto(null, "TestData-3", 3000000L));
        crudService.create(new TestDto(null, "TestData-4", 4000000L));
        crudService.create(new TestDto(null, "TestData-5", 5000000L));
    }

    @Test
    public void create(){
        TestDto testDto = new TestDto(null, "SomeInfo", 12345L);
        crudService.create(testDto);
        testDto.setId(6L);
        Assertions.assertEquals(testDto, crudService.read(6L));
        Assertions.assertEquals(testDto.getExampleFieldOne(), crudService.read(6L).getExampleFieldOne());
        Assertions.assertEquals(testDto.getExampleFieldTwo(), crudService.read(6L).getExampleFieldTwo());
        Assertions.assertNotEquals(testDto, crudService.read(5L));
        try { // тест на проверку выброса исключения, если testDto имеет id
            testDto.setId(1L);
            crudService.create(testDto);
        }catch (Exception e){
            Assertions.assertEquals("Попытка создать новую запись с указанным id="+testDto.getId(), e.getMessage());
        }
    }

    @Test
    public void read(){
        TestDto readingTestDto = crudService.read(3L);
        Assertions.assertEquals(readingTestDto, crudService.read(3L));
        Assertions.assertEquals("TestData-3", crudService.read(3L).getExampleFieldOne());
        Assertions.assertEquals(3000000, crudService.read(3L).getExampleFieldTwo());
        try{ // тест на проверку выброса исключения, если не указан id
            crudService.read(null);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
        try{ // тест на проверку выброса исключения, если id 0
            crudService.read(0L);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
    }

    @Test
    public void update() throws Exception {
        TestDto testDto = new TestDto(2L, "Updated-Information", 12345L);

        TestDto update = crudService.update(testDto, 2L);   // не работает через crudService
        Assertions.assertNotNull(testDto);
        Assertions.assertEquals(testDto, crudService.read(2L));
        Assertions.assertEquals(testDto.getExampleFieldOne(), crudService.read(2L).getExampleFieldOne());
        Assertions.assertEquals(testDto.getExampleFieldTwo(), crudService.read(2L).getExampleFieldTwo());
        try {  // тест на проверку выброса исключения, если не указан id
            crudService.update( testDto,null);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
        try{ // тест на проверку выброса исключения, если id 0
            crudService.update( testDto,0L);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
        try {  // тест на проверку выброса исключения, если не указаны данные
            crudService.update(null, 1L);
        } catch (Exception e){
            Assertions.assertEquals("java.lang.IllegalArgumentException: Не указан Dto", e.toString());
        }
    }

    @Test
    public void delete(){
        crudService.delete(4L);
        try {
            Assertions.assertNull(crudService.read(4L));
        }   catch (Exception ignored){}
        try {  // тест на проверку выброса исключения, если не указан id
            crudService.delete(null);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
        try{ // тест на проверку выброса исключения, если id 0
            crudService.delete(0L);
        } catch (Exception e){
            Assertions.assertEquals("Не указан Id", e.getMessage());
        }
    }
}