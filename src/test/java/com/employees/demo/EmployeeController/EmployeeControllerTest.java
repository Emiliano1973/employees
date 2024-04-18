package com.employees.demo.EmployeeController;

import com.employees.demo.controllers.EmployeeController;
import com.employees.demo.dtos.*;
import com.employees.demo.services.EmployeeNotFoundException;
import com.employees.demo.services.EmployeeService;
import com.employees.demo.utils.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class EmployeeControllerTest {

    private static final Integer PAGE_NUMBER = Integer.valueOf(1);
    private static final Integer PAGE_SIZE = Integer.valueOf(20);
    private static final Long EMP_NUMBER = Long.valueOf(10001L);

    private static final String ORDER_BY_TEST = "employeeNumber";

    private static final String ORDER_BY_DIR_TEST = "ASC";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeAll
    public static void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnFirstPageWhenEmPageCalled() throws Exception {
        when(this.employeeService.findByPage(new PaginationRequestDto(PAGE_NUMBER, PAGE_SIZE, ORDER_BY_TEST, ORDER_BY_DIR_TEST, Optional.empty()))).thenReturn(getPaginationDto());
        mockMvc.perform(get("/api/services/employees/pages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE_NUMBER))
                        .param("pageSize", String.valueOf(PAGE_SIZE))
                        .param("orderBy", ORDER_BY_TEST)
                        .param("orderByDir", ORDER_BY_DIR_TEST)
                ).andDo(print()).
                andExpect(status().isOk()).andExpect(jsonPath("$.elements").exists())
                .andExpect(jsonPath("$.elements[*].employeeNumber").isNotEmpty());
        // .param
    }

    @Test
    public void shouldReturnEmptyWhenEmPageCalled() throws Exception {
        when(this.employeeService.findByPage(new PaginationRequestDto(PAGE_NUMBER, PAGE_SIZE, ORDER_BY_TEST,
                ORDER_BY_DIR_TEST, Optional.empty()))).thenReturn(getEmptyPaginationDto());
        mockMvc.perform(get("/api/services/employees/pages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(PAGE_NUMBER))
                        .param("pageSize", String.valueOf(PAGE_SIZE))
                        .param("orderBy", ORDER_BY_TEST)
                        .param("orderByDir", ORDER_BY_DIR_TEST)
                ).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.elements").exists())
                .andExpect(jsonPath("$.elements[*].employeeNumber").isEmpty());
        // .param
    }

    @Test
    public void shouldReturnEmployeeDtoWhenUseEmployeeNumber() throws Exception {
        when(this.employeeService.findByEmpNum(EMP_NUMBER)).thenReturn(Optional.of(getEmployeeDto()));
        mockMvc.perform(get("/api/services/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeNumber").exists())
                .andExpect(jsonPath("$.employeeNumber").isNumber())
                .andExpect(jsonPath("$.employeeNumber").value(EMP_NUMBER));
    }

    @Test
    public void shouldReturnCode201DtoWhenNewEmployeeIsAdded() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        mockMvc.perform(post("/api/services/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(employeeDto))
                ).andDo(print()).
                andExpect(status().isCreated());
        verify(this.employeeService).insertNewEmployee(employeeDto);
    }

    @Test
    public void shouldReturnCodeOKDtoWhenNewEmployeeIsUpdated() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        mockMvc.perform(put("/api/services/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(employeeDto))
                ).andDo(print()).
                andExpect(status().isNoContent());
        verify(this.employeeService).updateEmployee(EMP_NUMBER, employeeDto);
    }

    @Test
    public void shouldReturnCode404CodeDtoWhenNewEmployeesDorUpdateIsNoFound() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        doThrow(new EmployeeNotFoundException(EMP_NUMBER)).when(this.employeeService).updateEmployee(EMP_NUMBER, employeeDto);
        mockMvc.perform(put("/api/services/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(employeeDto))
                ).andDo(print()).
                andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Employee not found for [" + EMP_NUMBER + "] emp number"));

    }

    @Test
    public void shouldReturnHttpCode404WhenUseEployeeNumberIsNotFound() throws Exception {
        when(this.employeeService.findByEmpNum(EMP_NUMBER)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/services/employees/{empNo}", EMP_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print()).
                andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Employee not found for [" + EMP_NUMBER + "] emp number"));
    }

    @Test
    public void shouldDeleteEmployByItsEmpNumber() throws Exception {
        mockMvc.perform(delete("/api/services/employees/{empNum}", EMP_NUMBER)
                ).andDo(print()).
                andExpect(status().isNoContent());
        verify(this.employeeService).deleteEmployee(EMP_NUMBER);
    }

    private PaginationDto getPaginationDto() {
        return new PaginatorDtoBuilder().setCurrentPageTotalElements(1)
                .setCurrentPage(PAGE_NUMBER).setTotalPages(1).setTotalPages(1)
                .setPageSize(PAGE_SIZE).setElements(List.of(getEmployeeListItemDto()))
                .setTotalPages(1).createPaginatorDto();
    }


    private PaginationDto getEmptyPaginationDto() {
        return new PaginatorDtoBuilder().setCurrentPageTotalElements(0)
                .setCurrentPage(0).setTotalPages(0).setTotalElements(0)
                .setPageSize(0).setElements(Collections.emptyList())
                .setTotalPages(1).createPaginatorDto();

    }

    private EmployeeListItemDto getEmployeeListItemDto() {
        return new EmployeeListItemDto(EMP_NUMBER, "Ciro", "Esposito", LocalDate.now(), "department", "title");
    }

    private EmployeeDto getEmployeeDto() {
        return new EmployeeDto(EMP_NUMBER, "Ciro", "Esposito", Gender.MALE, LocalDate.now(), LocalDate.now(), "department", 12000, "title");
    }
}
