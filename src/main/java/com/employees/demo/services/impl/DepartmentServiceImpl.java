package com.employees.demo.services.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.services.DepartmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao departmentDao;

    public DepartmentServiceImpl(final DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public ResponseDto getAllDepartments() {
        Collection<DropDownDto> dropDownDtos= this.departmentDao.getAllDepartments();
        return new ResponseDto(dropDownDtos.size(), dropDownDtos);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public ResponseDto getEmployeesDeptGroups() {
        Collection<Object[]> employeesPieTmp=this.departmentDao.getEmployeesDeptGroups();
        Collection<Object[]> employeesPie=new ArrayList<>(List.<Object[]>of(new String[]{"Departments", "Perc. of Employees"}));
        employeesPie.addAll(employeesPieTmp);
        return new ResponseDto(employeesPie.size(), employeesPie);
    }
}
