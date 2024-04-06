package com.employees.demo.controllers;

import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;
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

@RestController
@RequestMapping("/api/services/employees" )
@CacheConfig(cacheNames = "employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @Cacheable(value="employees", key ="#empNumber" )
    @GetMapping(value = "/{empNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByEmpNo(@PathVariable("empNo") final Long empNumber){
        return ResponseEntity.of(this.employeeService.findByEmpNum(empNumber));
    }

    @GetMapping(value = "/pages", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaginationDto getPage(@RequestParam("page") final int page,
                                      @RequestParam("pageSize") final  int pageSize){
        return  this.employeeService.findByPage(page, pageSize);

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertNewEmployee(@Valid @RequestBody final EmployeeDto employeeDto){
        this.employeeService.insertNewEmployee(employeeDto);
        URI location = ServletUriComponentsBuilder.fromUriString("/employees").build()
                .toUri();
        return ResponseEntity.created(location).build();
   }

    @CacheEvict(value = "employees", key ="#empNumber" )
    @PutMapping(value = "/{empNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEmployee(@PathVariable("empNo") final Long empNumber,
                                            @Valid @RequestBody final EmployeeDto employeeDto){
        this.employeeService.updateEmployee(empNumber, employeeDto);
        return ResponseEntity.noContent().build();
   }

    @CacheEvict(value = "employees", key ="#empNumber" )
    @DeleteMapping(value = "/{empNo}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empNo") final Long empNumber){
        this.employeeService.deleteEmployee(empNumber);
        return ResponseEntity.noContent().build();
    }

}
