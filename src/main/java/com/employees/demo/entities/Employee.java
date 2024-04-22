package com.employees.demo.entities;

import com.employees.demo.utils.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
@Setter
@Getter
@NoArgsConstructor
public class Employee implements Serializable {

    @Id
    @Column(name = "emp_no")
    private long employeeNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;


    @Column(name = "gender")
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<DeptEmp> departments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "manager", fetch = FetchType.LAZY)
    private Set<DeptManager> departmentManaged = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<Title> titles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<Salary> salaries = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Employee employee = (Employee) o;
        return Objects.equals(employeeNumber, employee.employeeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber);
    }
}
