package com.employees.demo.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class EmpDeptsPk implements Serializable {

    @Column(name = "emp_no", nullable = false)
    private long employeeNumber;


    @Column(name = "dept_no", nullable = false)
    private String departmentNumber;

    @Column(name = "from_date")
    private LocalDate fromDate;


    public EmpDeptsPk(final long employeeNumber,final String departmentNumber,final LocalDate fromDate) {
        this.employeeNumber = employeeNumber;
        this.departmentNumber = departmentNumber;
        this.fromDate=fromDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EmpDeptsPk that = (EmpDeptsPk) o;
        return Objects.equals(employeeNumber, that.employeeNumber)
                && Objects.equals(departmentNumber, that.departmentNumber)
                && Objects.equals(fromDate, that.fromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber, departmentNumber, fromDate);
    }
}
