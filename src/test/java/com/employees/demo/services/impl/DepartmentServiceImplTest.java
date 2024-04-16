package com.employees.demo.services.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.dtos.ResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class) // or @SpringBootTest
@SpringBootTest
public class DepartmentServiceImplTest {


    private static final String CODE = "code";
    private static final String DESC = "desc";

    @Autowired
    private DepartmentServiceImpl departmentService;
    @MockBean
    private DepartmentDao departmentDao;

    @Test
    public void shouldReturnAllDepartments() throws Exception {
        DropDownDto dropDownDto = new DropDownDto(CODE, DESC);
        Mockito.when(departmentDao.getAllDepartments()).thenReturn(List.of(dropDownDto));
        ResponseDto responseDto = this.departmentService.getAllDepartments();
        assertNotNull(responseDto);
        assertEquals(1, responseDto.totalElements());
        DropDownDto dropDownDtoOut = (DropDownDto) responseDto.elements().iterator().next();
        assertEquals(CODE, dropDownDtoOut.code());
        assertEquals(DESC, dropDownDtoOut.description());

    }

}
