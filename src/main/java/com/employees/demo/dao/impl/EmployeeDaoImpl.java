package com.employees.demo.dao.impl;

import com.employees.demo.dao.EmployeeDao;
import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.EmployeeListItemDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.dtos.PaginatorDtoBuilder;
import com.employees.demo.entities.Department;
import com.employees.demo.entities.Department_;
import com.employees.demo.entities.DeptEmp;
import com.employees.demo.entities.DeptEmp_;
import com.employees.demo.entities.Employee;
import com.employees.demo.entities.Employee_;
import com.employees.demo.entities.Salary;
import com.employees.demo.entities.Salary_;
import com.employees.demo.entities.Title;
import com.employees.demo.entities.Title_;
import com.employees.demo.entities.pk.TitlePk_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    @PersistenceContext
    private EntityManager em;

    private static final Integer YEAR_FAKE_END=Integer.valueOf(9999);

    @Override
    public PaginationDto findPages(final int page,final int pageSize) {
        final CriteriaBuilder cb=this.em.getCriteriaBuilder();
        int countEmp=countEmp(cb);
        if (countEmp == 0) {
            return new PaginatorDtoBuilder().setCurrentPage(page).setCurrentPageTotalElements(0).setTotalPages(0).setPageSize(pageSize)
                    .setTotalElements(0).setElements(new ArrayList<>()).createPaginatorDto();
        }
        CriteriaQuery<EmployeeListItemDto>  criteriaQuery=cb.createQuery(EmployeeListItemDto.class);
        Root<Employee> employeeRoot=criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin=employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin=empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Title> titleJoin=employeeRoot.join(Employee_.titles, JoinType.INNER);
        criteriaQuery.multiselect(
                employeeRoot.get(Employee_.employeeNumber),
                employeeRoot.get(Employee_.firstName),
                employeeRoot.get(Employee_.lastName),
                employeeRoot.get(Employee_.gender),
                employeeRoot.get(Employee_.birthDate),
                employeeRoot.get(Employee_.hireDate),
                empDepartmentJoin.get(Department_.departmentName),
                titleJoin.get(Title_.titleId).get(TitlePk_.title)
                ).orderBy(cb.asc(employeeRoot.get(Employee_.employeeNumber)));
        TypedQuery<EmployeeListItemDto> query = this.em.createQuery(criteriaQuery);
        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        Collection<EmployeeListItemDto> employeeListItemDtos = query.getResultList();
        int numPages = (countEmp / pageSize);
        if ((countEmp % pageSize) > 0) {
            numPages += 1;
        }
        return new PaginatorDtoBuilder().setCurrentPage(page)
                .setCurrentPageTotalElements(employeeListItemDtos.size()).setTotalPages(numPages)
                .setPageSize(pageSize).setTotalElements(countEmp).setElements(employeeListItemDtos)
                .createPaginatorDto();
    }

    @Override
    public Optional<EmployeeDto> findByEmpNumber(final Long empNumber) {
        final CriteriaBuilder cb=this.em.getCriteriaBuilder();
        CriteriaQuery<EmployeeDto>  criteriaQuery=cb.createQuery(EmployeeDto.class);
        Root<Employee> employeeRoot=criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin=employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin=empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Salary> employeeSalaryJoin=employeeRoot.join(Employee_.salaries, JoinType.INNER);
        Join<Employee, Title> titleJoin=employeeRoot.join(Employee_.titles, JoinType.INNER);
        Expression<Integer>  yearSal= cb.function("year", Integer.class, employeeSalaryJoin.get(Salary_.toDate));
        Expression<Integer>  yearTitle= cb.function("year", Integer.class, titleJoin.get(Title_.toDate));
        criteriaQuery.multiselect(
                employeeRoot.get(Employee_.employeeNumber),
                employeeRoot.get(Employee_.firstName),
                employeeRoot.get(Employee_.lastName),
                employeeRoot.get(Employee_.gender),
                employeeRoot.get(Employee_.birthDate),
                employeeRoot.get(Employee_.hireDate),
                empDepartmentJoin.get(Department_.departmentNumber),
                employeeSalaryJoin.get(Salary_.salary),
                titleJoin.get(Title_.titleId).get(TitlePk_.title)
        ).where(cb.equal(yearSal, cb.literal(YEAR_FAKE_END) ),
                cb.equal(yearTitle,
                cb.literal(YEAR_FAKE_END) ), cb.equal(employeeRoot.get(Employee_.employeeNumber), empNumber));
        TypedQuery<EmployeeDto> query = this.em.createQuery(criteriaQuery);
        return  query.getResultStream().findFirst();
    }

    @Override
    public long findMaxEmployeeNumber() {
        final CriteriaBuilder cb=this.em.getCriteriaBuilder();
        CriteriaQuery<Long>  criteriaQuery=cb.createQuery(Long.class);
        Root<Employee> employeeRoot=criteriaQuery.from(Employee.class);
        criteriaQuery.select(cb.max(employeeRoot.get(Employee_.EMPLOYEE_NUMBER)));
        return this.em.createQuery(criteriaQuery).getSingleResult().longValue();
    }

    private int countEmp(final CriteriaBuilder cb){
        CriteriaQuery<Long>  criteriaQuery=cb.createQuery(Long.class);
        Root<Employee> employeeRoot=criteriaQuery.from(Employee.class);
        criteriaQuery.select(cb.count(employeeRoot.get(Employee_.EMPLOYEE_NUMBER)));
       return this.em.createQuery(criteriaQuery).getSingleResult().intValue();
    }

}
