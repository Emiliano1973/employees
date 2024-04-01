package com.employees.demo.entities;

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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "departments")
@Setter @Getter @NoArgsConstructor
public class Department implements Serializable {

    @Id
    @Column(name = "dept_no")
    private String departmentNumber;

    @Column(name = "dept_name")
    private String departmentName;

    @OneToMany( orphanRemoval = true, mappedBy = "department", fetch = FetchType.LAZY)
    private Set<DeptEmp>  employees=new HashSet<>();

    @OneToMany(  orphanRemoval = true, mappedBy = "department", fetch = FetchType.LAZY)
    private Set<DeptManager> managers=new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(departmentNumber, that.departmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentNumber);
    }
}
