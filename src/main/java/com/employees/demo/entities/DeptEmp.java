package com.employees.demo.entities;

import com.employees.demo.entities.pk.EmpDeptsPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "dept_emp")
@Getter
@Setter
@NoArgsConstructor
public class DeptEmp implements Serializable {

    @EmbeddedId
    private EmpDeptsPk empDeptsId;


    @Column(name = "to_date")
    private LocalDate toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "dept_no", insertable = false, updatable = false)
    private Department department;


    public DeptEmp(final long employeeNumber, final String departmentNumber, final LocalDate fromDate, LocalDate toDate) {
        this.empDeptsId = new EmpDeptsPk(employeeNumber, departmentNumber, fromDate);
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptEmp deptEmp = (DeptEmp) o;
        return Objects.equals(empDeptsId, deptEmp.empDeptsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empDeptsId);
    }
}
