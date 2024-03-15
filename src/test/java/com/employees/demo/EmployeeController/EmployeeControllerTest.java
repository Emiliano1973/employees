package com.employees.demo.EmployeeController;

import com.employees.demo.controllers.EmployeeController;
import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.EmployeeListItemDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.dtos.PaginatorDtoBuilder;
import com.employees.demo.services.EmployeeService;
import com.employees.demo.utils.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    private static final Integer PAGE_NUMBER=Integer.valueOf(1);
    private static final Integer PAGE_SIZE=Integer.valueOf(20);

    private static final Long EMP_NUMBER=Long.valueOf(10001);
    private static final ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeAll
    public static void setup(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @Test
    public void shouldReturnFirstPageWhenEmPageCalled() throws Exception{
        when(this.employeeService.findByPage(PAGE_NUMBER, PAGE_SIZE)).thenReturn(getPaginationDto());
        mockMvc.perform(get("/employees/pages")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(PAGE_NUMBER))
                .param("pageSize", String.valueOf(PAGE_SIZE))
                ).andDo(print()).
                andExpect(status().isOk()).andExpect(jsonPath("$.elements").exists())
                .andExpect(jsonPath("$.elements[*].employeeNumber").isNotEmpty())        ;
           // .param
    }


    @Test
    public void shouldReturnEmptyWhenEmPageCalled() throws Exception{
        when(this.employeeService.findByPage(PAGE_NUMBER, PAGE_SIZE)).thenReturn(getEmptyPaginationDto());
        mockMvc.perform(get("/employees/pages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE_NUMBER))
                        .param("pageSize", String.valueOf(PAGE_SIZE))
                ).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").exists())
                .andExpect(jsonPath("$.elements[*].employeeNumber").isEmpty())        ;
        // .param
    }


    @Test
    public void shouldReturnEmployeeDtoWhenUseEployeeNumber() throws Exception{
        when(this.employeeService.findByEmpNum(EMP_NUMBER)).thenReturn(Optional.of(getEmployeeDto()));
        mockMvc.perform(get("/employees/{empNo}",EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeNumber").exists())
                .andExpect(jsonPath("$.employeeNumber").isNumber())
                .andExpect(jsonPath("$.employeeNumber").value(EMP_NUMBER));
    }


    @Test
    public void shouldReturnCode201DtoWhenNewEmployeeIsAdded() throws Exception{
        EmployeeDto employeeDto=getEmployeeDto();
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(employeeDto))
                ).andDo(print()).
                andExpect(status().isCreated());
                verify(this.employeeService).insertNewEmployee(employeeDto);
    }

    @Test
    public void shouldReturnCodeOKDtoWhenNewEmployeeIsUpdated() throws Exception{
        EmployeeDto employeeDto=getEmployeeDto();
        mockMvc.perform(put("/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(employeeDto))
                ).andDo(print()).
                andExpect(status().isOk());
        verify(this.employeeService).updateEmployee(EMP_NUMBER,employeeDto);
    }

    @Test
    public void shouldReturnHttpCode404WhenUseEployeeNumberIsNotFound() throws Exception {
        when(this.employeeService.findByEmpNum(EMP_NUMBER)).thenReturn(Optional.empty());
        mockMvc.perform(get("/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteEmployByItsEmpNumber() throws Exception{
        mockMvc.perform(delete("/employees/{empNum}", EMP_NUMBER)
                ).andDo(print()).
                andExpect(status().isOk()) ;
        verify(this.employeeService).deleteEmployee(EMP_NUMBER);
    }

        public static  String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PaginationDto getPaginationDto(){
        return new PaginatorDtoBuilder().setCurrentPageTotalElements(1)
                .setCurrentPage(PAGE_NUMBER).setTotalPages(1).setTotalPages(1)
                .setPageSize(PAGE_SIZE).setElements( List.of(getEmployeeListItemDto()))
                .setTotalPages(1).createPaginatorDto();
    }


    private PaginationDto getEmptyPaginationDto(){
        return new PaginatorDtoBuilder().setCurrentPageTotalElements(0)
                .setCurrentPage(0).setTotalPages(0).setTotalElements(0)
                .setPageSize(0).setElements( Collections.emptyList())
                .setTotalPages(1).createPaginatorDto();

    }

    private EmployeeListItemDto getEmployeeListItemDto(){
        return new EmployeeListItemDto(EMP_NUMBER,"Ciro", "Esposito",Gender.MALE, LocalDate.now(), LocalDate.now(), "deparment", "title");
    }

    private EmployeeDto getEmployeeDto(){
        return new EmployeeDto(EMP_NUMBER,"Ciro", "Esposito",Gender.MALE, LocalDate.now(), LocalDate.now(), "deparment", 12000, "title");
    }
}
