package com.employees.demo.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmpDeptsPk implements Serializable {

    @Column(name = "emp_no", nullable = false)
    private long employeeNumber;


    @Column(name = "dept_no", nullable = false)
    private String departmentNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EmpDeptsPk that = (EmpDeptsPk) o;
        return Objects.equals(employeeNumber, that.employeeNumber) && Objects.equals(departmentNumber, that.departmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber, departmentNumber);
    }
}