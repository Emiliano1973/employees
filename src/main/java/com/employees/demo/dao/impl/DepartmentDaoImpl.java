package com.employees.demo.dao.impl;

import com.employees.demo.dao.DepartmentDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.entities.Department;
import com.employees.demo.entities.Department_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Collection<DropDownDto> getAllDepartments() {
        CriteriaBuilder cb=this.em.getCriteriaBuilder();
        CriteriaQuery<DropDownDto> criteriaQuery=cb.createQuery(DropDownDto.class);
        Root<Department> departmentRoot=criteriaQuery.from(Department.class);
        criteriaQuery
                .multiselect(departmentRoot.get(Department_.departmentNumber),
                        departmentRoot.get(Department_.departmentName))
                .orderBy( cb.asc(departmentRoot.get(Department_.departmentName)));
        return  this.em.createQuery(criteriaQuery).getResultList();
    }
}
