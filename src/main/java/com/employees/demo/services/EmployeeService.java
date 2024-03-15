package com.employees.demo.services;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    PaginationDto findByPage(int page, int pageSize);

    Optional<EmployeeDto> findByEmpNum(Long empNum);

    void insertNewEmployee(EmployeeDto employeeDto);

    void updateEmployee(Long empNum ,EmployeeDto employeeDto);

    void deleteEmployee(Long empNum);

}
