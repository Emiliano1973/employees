package com.employees.demo.EmployeeController;

import com.employees.demo.controllers.DepartmentController;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.services.DepartmentService;
import com.employees.demo.services.DownloadManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartmentController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

public class DepartmentControllerTest {

    private static final String CODE = "code";
    private static final String DESC = "desc";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DownloadManagerService jasperDownloadManagerService;


    @Test
    public void shouldReturnDepartmentsList() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(initDropDownDto());
        mockMvc.perform(get("/api/services/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print()).
                andExpect(status().isOk()).andExpect(jsonPath("$.elements").exists())
                .andExpect(jsonPath("$.elements[*].code").value(CODE));
    }

    private ResponseDto initDropDownDto() {
        return new ResponseDto(1, List.of(new DropDownDto(CODE, DESC)));
    }


}
