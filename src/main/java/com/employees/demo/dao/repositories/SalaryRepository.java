package com.employees.demo.dao.repositories;

import com.employees.demo.entities.Salary;
import com.employees.demo.entities.pk.SalaryPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, SalaryPk> {

    void deleteBySalaryIdEmployeeNumber(Long employeeNumber);
}
