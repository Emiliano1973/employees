package com.employees.demo.entities;

import com.employees.demo.entities.pk.EmpDeptsPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "dept_manager")
@Getter
@Setter
public class DeptManager implements Serializable {

    @EmbeddedId
    private EmpDeptsPk deptManagerId;

    @Column(name = "to_date")
    private LocalDate toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_no", insertable = false, updatable = false)
    private Department department;

    public DeptManager(long employeeNumber, String departmentNumber, LocalDate fromDate, LocalDate toDate) {
        this.deptManagerId = new EmpDeptsPk(employeeNumber, departmentNumber, fromDate);
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeptManager that = (DeptManager) o;
        return Objects.equals(deptManagerId, that.deptManagerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptManagerId);
    }
}
