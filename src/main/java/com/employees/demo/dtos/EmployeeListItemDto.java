package com.employees.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;

public record EmployeeListItemDto(Long employeeNumber,
                                  String firstName,
                                  String lastName,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
                                  LocalDate hireDate,
                                  String departmentName,
                                  String title
                                  ) implements Serializable {
}
