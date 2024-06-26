package com.employees.demo.entities.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitlePk implements Serializable {


    @Column(name = "emp_no", nullable = false)
    private long employeeNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TitlePk titlePk = (TitlePk) o;
        return Objects.equals(employeeNumber, titlePk.employeeNumber) && Objects.equals(title, titlePk.title) && Objects.equals(fromDate, titlePk.fromDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber, title, fromDate);
    }
}
