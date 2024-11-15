package com.employees.demo.controllers;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationRequestDto;
import com.employees.demo.services.EmployeeNotFoundException;
import com.employees.demo.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @Cacheable("employees")
    @GetMapping(value = "/{empNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto getByEmpNo(@PathVariable("empNo") final Long empNo) {
        EmployeeDto employeeDto = this.employeeService
                .findByEmpNum(empNo).orElseThrow(() -> new EmployeeNotFoundException(empNo));
        return employeeDto;
    }

    @GetMapping(value = "/pages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPage(@RequestParam("page") final int page,
                                     @RequestParam("pageSize") final int pageSize,
                                     @RequestParam(value = "orderBy",
                                             defaultValue = "employeeNumber") final String orderBy,
                                     @RequestParam(value = "orderByDir",
                                             defaultValue = "ASC") final String orderByDir,
                                     @RequestParam(value = "searchLike",
                                             required = false) final String searchLike) {
        PaginationRequestDto request = new PaginationRequestDto(page, pageSize, orderBy,
                orderByDir, Optional.ofNullable(searchLike));
        return ResponseEntity.ok(this.employeeService.findByPage(request));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertNewEmployee(@Valid @RequestBody final EmployeeDto employeeDto) {
        this.employeeService.insertNewEmployee(employeeDto);
        URI location = ServletUriComponentsBuilder.fromUriString("/employees").build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @CacheEvict(value = "employees", key = "#empNo")
    @PutMapping(value = "/{empNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable("empNo") final Long empNo,
                                            @Valid @RequestBody final EmployeeDto employeeDto) {
        this.employeeService.updateEmployee(empNo, employeeDto);
        return ResponseEntity.noContent().build();
    }

    @CacheEvict(value = "employees", key = "#empNo")
    @DeleteMapping(value = "/{empNo}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empNo") final Long empNo) {
        this.employeeService.deleteEmployee(empNo);
        return ResponseEntity.noContent().build();
    }

}
