package com.employees.demo.services.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(final DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public ResponseDto getAllDepartments() {
        Collection<DropDownDto> dropDownDtos= this.departmentDao.getAllDepartments();
        return new ResponseDto(dropDownDtos.size(), dropDownDtos);
    }
}
