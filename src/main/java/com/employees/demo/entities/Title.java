package com.employees.demo.entities;

import com.employees.demo.entities.pk.TitlePk;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "titles")
@Getter
@Setter
@NoArgsConstructor
public class Title implements Serializable {
    @EmbeddedId
    private TitlePk titleId;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "to_date")
    private LocalDate toDate;

    public Title(long employeeNumber, String title, LocalDate fromDate, LocalDate toDate) {
        this.titleId = new TitlePk(employeeNumber, title, fromDate);
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(titleId, title.titleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titleId);
    }
}
