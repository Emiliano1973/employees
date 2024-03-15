package com.employees.demo.dao.repositories;

import com.employees.demo.entities.DeptEmp;
import com.employees.demo.entities.pk.EmpDeptsPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, EmpDeptsPk> {
}
