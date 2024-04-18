package com.employees.demo.dao;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.dtos.PaginationRequestDto;

import java.util.Optional;

public interface EmployeeDao {


    PaginationDto findPages(PaginationRequestDto request);

    Optional<EmployeeDto> findByEmpNumber(Long empNumber);

    long findMaxEmployeeNumber();

}