package com.employees.demo.services;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long employeeNumber) {
        super("Employee not found for [" + employeeNumber + "] emp number");
    }


}
