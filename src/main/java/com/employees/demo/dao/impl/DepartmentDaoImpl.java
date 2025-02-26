package com.employees.demo.dao.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
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
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<DropDownDto> criteriaQuery = criteriaBuilder.createQuery(DropDownDto.class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        criteriaQuery
                .multiselect(departmentRoot.get(Department_.departmentNumber),
                        departmentRoot.get(Department_.departmentName))
                .orderBy(criteriaBuilder.asc(departmentRoot.get(Department_.departmentName)));
        return this.em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Collection<Object[]> getEmployeesDeptGroups() {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Department> departmentRoot = criteriaQuery.from(Department.class);
        Join<Department, DeptEmp> departmentDeptEmpJoin = departmentRoot.join(Department_.employees, JoinType.INNER);
        Join<DeptEmp, Employee> deptEmpEmployeeJoin = departmentDeptEmpJoin.join(DeptEmp_.employee, JoinType.INNER);
        criteriaQuery.multiselect(departmentRoot.get(Department_.departmentName),
                        criteriaBuilder.countDistinct(deptEmpEmployeeJoin.get(Employee_.employeeNumber)))
                .where(criteriaBuilder.equal(departmentDeptEmpJoin.get(DeptEmp_.toDate), DATE_FAKE_END))
                .groupBy(departmentRoot.get(Department_.departmentName));
        return this.em.createQuery(criteriaQuery).getResultList();
    }
}
