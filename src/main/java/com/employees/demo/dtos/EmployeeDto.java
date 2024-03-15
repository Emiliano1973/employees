package com.employees.demo.dtos;

import com.employees.demo.utils.Gender;
import com.employees.demo.utils.GenderJsonDeserializer;
import com.employees.demo.utils.GenderJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data @NoArgsConstructor
public class EmployeeDto  implements Serializable {

    private Long employeeNumber;

    @NotBlank(message = "First name cannot be blank")
    @NotEmpty(message = "First name cannot be empty")
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    @NotEmpty(message = "Last name cannot be empty")
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Gender cannot be null")
    @JsonSerialize(using = GenderJsonSerializer.class)
    @JsonDeserialize(using = GenderJsonDeserializer.class)
    private Gender gender;
    @NotNull(message = "Birth date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    @NotNull(message = "Hire date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate hireDate;
    @NotBlank(message = "Department number cannot be blank")
    @NotEmpty(message = "Department number cannot be empty")
    @NotNull(message = "Department number cannot be null")
    private String departmentNumber;
    @NotNull(message = "Salary cannot be null")
    private Integer salary;
    @NotBlank(message = "Title cannot be blank")
    @NotEmpty(message = "Title cannot be empty")
    @NotNull(message = "Title cannot be null")
    private String title;


    public EmployeeDto(Long employeeNumber, String firstName, String lastName, Gender gender, LocalDate birthDate, LocalDate hireDate, String departmentNumber, Integer salary, String title) {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.departmentNumber = departmentNumber;
        this.salary = salary;
        this.title=title;
    }
}
