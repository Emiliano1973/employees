package com.employees.demo.dtos;

import com.employees.demo.utils.Gender;
import com.employees.demo.utils.GenderJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDate;

public record EmployeeListItemDto(Long employeeNumber,
                                  String firstName,
                                  String lastName,
                                  @JsonSerialize(using = GenderJsonSerializer.class)    Gender gender,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
                                  LocalDate birthDate,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
                                  LocalDate hireDate,
                                  String departmentName,
                                  String title
                                  ) implements Serializable {
}
