/*
package ru.ibs.crud.tests.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.ibs.crud.spring.controller.DefaultCrudController;
import ru.ibs.crud.spring.dto.ResponseDto;
import ru.ibs.crud.spring.mapper.Mapper;
import ru.ibs.crud.spring.services.AbstractFindService;
import ru.ibs.crud.spring.services.FindService;
import ru.ibs.crud.tests.dto.TestDto;
import ru.ibs.crud.tests.entity.TestEntity;
import ru.ibs.crud.tests.repo.MockRepo;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = DefaultCrudController.class)
@ContextConfiguration(classes = TestConfiguration.class)
class DefaultFindControllerTest {

   @TestConfiguration
    static class LocalTestContextConfiguration{
        @Bean
        public FindService<TestDto> findService(JpaRepository<TestEntity, Long> repository,
                                                Mapper<TestEntity, TestDto> mapper) {
            return new AbstractFindService<>(repository, mapper) {};
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindService<TestDto> findService;

    @MockBean
    private MockRepo mockRepo;

//    @Test
//    void findResponse() throws Exception {
//        TestDto testDto1 = new TestDto(null, "TestData-1", 1000000L);
//        Mockito.when(mockRepo.findById(1L)).thenReturn(Optional.of(testDto1));
//
//        mockMvc.perform(
//                        get("/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.exampleFieldOne").value("TestData-1"))
//                .andExpect(jsonPath("$.exampleFieldTwo").value("1000000"));
//    }

    @Test
    void findResponseTest() throws Exception {
        TestDto testDto1 = new TestDto(1L, "TestData-1", 1000000L);
        List<TestDto> testDtoList = new ArrayList<>();
        testDtoList.add(testDto1);
        ResponseDto<TestDto> testresponse = new ResponseDto<TestDto>(testDtoList, 1L);
//        ResponseDto<TestDto> allFindedDto = findService.find();
//        Mockito.when(mockRepo.findById()).thenReturn(testDto1);
        AbstractFindService findService = Mockito.mock(AbstractFindService.class);
        Mockito.when(findService.find()).thenReturn(testresponse);
        mockMvc.perform(
                        get("/find"))
               .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value("1"));
//                .andExpect(jsonPath("$.exampleFieldOne").value("TestData-1"))
//                .andExpect(jsonPath("$.exampleFieldTwo").value("1000000"));
    }

}


*/
