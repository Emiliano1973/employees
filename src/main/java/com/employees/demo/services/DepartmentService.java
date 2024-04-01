package com.employees.demo.services;

import com.employees.demo.dtos.ResponseDto;

public interface DepartmentService {

    ResponseDto getAllDepartments();

    ResponseDto getEmployeesDeptGroups();
}
