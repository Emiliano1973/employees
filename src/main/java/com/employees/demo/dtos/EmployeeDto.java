package com.employees.demo.dtos;

import com.employees.demo.utils.Gender;
import com.employees.demo.utils.GenderJsonDeserializer;
import com.employees.demo.utils.GenderJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record EmployeeDto(
     Long employeeNumber,

    @NotBlank(message = "First name cannot be blank")
     String firstName,
    @NotBlank(message = "Last name cannot be blank")
     String lastName,
    @NotNull(message = "Gender cannot be null")
    @JsonSerialize(using = GenderJsonSerializer.class)
    @JsonDeserialize(using = GenderJsonDeserializer.class)
     Gender gender,
    @NotNull(message = "Birth date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
     LocalDate birthDate,
    @NotNull(message = "Hire date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
     LocalDate hireDate,
    @NotBlank(message = "Department number cannot be blank")
     String departmentNumber,
    @NotNull(message = "Salary cannot be null")
     Integer salary,
    @NotBlank(message = "Title cannot be blank")
     String title
    )
 implements Serializable {

}
