package com.employees.demo.dtos;

import com.employees.demo.utils.Gender;
import com.employees.demo.utils.GenderJsonDeserializer;
import com.employees.demo.utils.GenderJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data @NoArgsConstructor
public class EmployeeDto  implements Serializable {

    private Long employeeNumber;
    private String firstName;
    private String lastName;
    @JsonSerialize(using = GenderJsonSerializer.class)
    @JsonDeserialize(using = GenderJsonDeserializer.class)
    private Gender gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate hireDate;
    private String departmentNumber;
    private Integer salary;
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
