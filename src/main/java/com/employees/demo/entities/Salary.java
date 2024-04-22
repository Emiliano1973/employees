package com.employees.demo.entities;

import com.employees.demo.entities.pk.SalaryPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "salaries")
@Getter
@Setter
@NoArgsConstructor
public class Salary implements Serializable {
    @EmbeddedId
    private SalaryPk salaryId;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "to_date")
    private LocalDate toDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    public Salary(long employeeNumber, Integer salary, LocalDate fromDate, LocalDate toDate) {
        this.salaryId = new SalaryPk(employeeNumber, fromDate);
        this.salary = salary;
        this.toDate = toDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salaryObj = (Salary) o;
        return Objects.equals(salaryId, salaryObj.salaryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salaryId);
    }
}
