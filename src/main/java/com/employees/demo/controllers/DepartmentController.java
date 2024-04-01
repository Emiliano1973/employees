package com.employees.demo.controllers;

import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.services.DepartmentService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services/departments")
@CacheConfig(cacheNames = "departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "departments", keyGenerator = "customKeyGenerator")
    public ResponseDto getAllDepartments(){
        return this.departmentService.getAllDepartments();
    }


    @GetMapping(value ="/pie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto getAllDepartmentsPie(){
        return this.departmentService.getEmployeesDeptGroups();
    }
}
