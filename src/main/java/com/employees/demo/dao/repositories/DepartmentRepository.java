package com.employees.demo.dao.repositories;

import com.employees.demo.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository  extends JpaRepository<Department, String> {
}
