package com.employees.demo.controllers;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.dtos.PaginationRequestDto;
import com.employees.demo.services.EmployeeNotFoundException;
import com.employees.demo.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/services/employees")
@CacheConfig(cacheNames = "employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Cacheable(value = "employees", key = "#empNumber")
    @GetMapping(value = "/{empNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByEmpNo(@PathVariable("empNo") final Long empNumber) {
        EmployeeDto employeeDto = this.employeeService
                .findByEmpNum(empNumber).orElseThrow(() -> new EmployeeNotFoundException(empNumber));
        return ResponseEntity.ok(employeeDto);
    }

    @GetMapping(value = "/pages", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginationDto getPage(@RequestParam("page") final int page,
                                 @RequestParam("pageSize") final int pageSize,
                                 @RequestParam(value = "orderBy",
            defaultValue = "employeeNumber") final String orderBy,
                                 @RequestParam(value = "orderByDir",
            defaultValue = "ASC") final String orderByDir,
                                 @RequestParam(value = "searchLike",
                                         required = false) final String searchLike) {
        PaginationRequestDto request = new PaginationRequestDto(page, pageSize, orderBy,
                orderByDir, Optional.ofNullable(searchLike));
        return this.employeeService.findByPage(request);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertNewEmployee(@Valid @RequestBody final EmployeeDto employeeDto) {
        this.employeeService.insertNewEmployee(employeeDto);
        URI location = ServletUriComponentsBuilder.fromUriString("/employees").build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @CacheEvict(value = "employees", key = "#empNumber")
    @PutMapping(value = "/{empNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable("empNo") final Long empNumber,
                                            @Valid @RequestBody final EmployeeDto employeeDto) {
        this.employeeService.updateEmployee(empNumber, employeeDto);
        return ResponseEntity.noContent().build();
    }

    @CacheEvict(value = "employees", key = "#empNumber")
    @DeleteMapping(value = "/{empNo}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empNo") final Long empNumber) {
        this.employeeService.deleteEmployee(empNumber);
        return ResponseEntity.noContent().build();
    }

}
