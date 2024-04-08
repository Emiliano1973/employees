package com.employees.demo.dao;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;

import java.util.Optional;

public interface EmployeeDao {


    PaginationDto findPages(int page, int pageSize,  String orderBy, String orderByDir);

    Optional<EmployeeDto> findByEmpNumber(Long empNumber);

    long findMaxEmployeeNumber();

}