package com.employees.demo.dao;

import com.employees.demo.dtos.DropDownDto;

import java.util.Collection;

public interface DepartmentDao {

    Collection<DropDownDto> getAllDepartments();
}
