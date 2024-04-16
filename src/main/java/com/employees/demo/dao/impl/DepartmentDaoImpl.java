package com.employees.demo.dao.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {

    private static final LocalDate DATE_FAKE_END = LocalDate.of(9999, 1, 1);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<DropDownDto> getAllDepartments() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<DropDownDto> criteriaQuery = cb.createQuery(DropDownDto.class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        criteriaQuery
                .multiselect(departmentRoot.get(Department_.departmentNumber),
                        departmentRoot.get(Department_.departmentName))
                .orderBy(cb.asc(departmentRoot.get(Department_.departmentName)));
        return this.em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Collection<Object[]> getEmployeesDeptGroups() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = cb.createQuery(Object[].class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        Join<Department, DeptEmp> departmentDeptEmpJoin = departmentRoot.join(Department_.employees, JoinType.INNER);
        Join<DeptEmp, Employee> deptEmpEmployeeJoin = departmentDeptEmpJoin.join(DeptEmp_.employee, JoinType.INNER);
        criteriaQuery.multiselect(departmentRoot.get(Department_.departmentName),
                        cb.countDistinct(deptEmpEmployeeJoin.get(Employee_.employeeNumber)))
                .where(cb.equal(departmentDeptEmpJoin.get(DeptEmp_.toDate), DATE_FAKE_END))
                .groupBy(departmentRoot.get(Department_.departmentName));
        return this.em.createQuery(criteriaQuery).getResultList();
    }
}
