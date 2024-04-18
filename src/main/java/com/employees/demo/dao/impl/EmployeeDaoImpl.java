package com.employees.demo.dao.impl;

import com.employees.demo.dao.EmployeeDao;
import com.employees.demo.dtos.*;
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
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private static final LocalDate DATE_FAKE_END = LocalDate.of(9999, 1, 1);
    @PersistenceContext
    private EntityManager em;

    @Override
    public PaginationDto findPages(final PaginationRequestDto request) {
        int page=request.page();
        int pageSize= request.pageSize();
        String orderBy=request.orderBy();
        String orderByDir= request.orderByDir();
        Optional<String> searchBy=request.searchLike();
        final CriteriaBuilder cb = this.em.getCriteriaBuilder();
        int countEmp = countEmp(cb, searchBy);
        if (countEmp == 0) {
            return new PaginatorDtoBuilder().setCurrentPage(page).setCurrentPageTotalElements(0).setTotalPages(0).setPageSize(pageSize)
                    .setTotalElements(0)
                    .setElements(new ArrayList<>()).createPaginatorDto();
        }
        CriteriaQuery<EmployeeListItemDto> criteriaQuery = cb.createQuery(EmployeeListItemDto.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin = employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin = empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Title> titleJoin = employeeRoot.join(Employee_.titles, JoinType.INNER);
        criteriaQuery.multiselect(
                employeeRoot.get(Employee_.employeeNumber),
                employeeRoot.get(Employee_.firstName),
                employeeRoot.get(Employee_.lastName),
                employeeRoot.get(Employee_.hireDate),
                empDepartmentJoin.get(Department_.departmentName),
                titleJoin.get(Title_.titleId).get(TitlePk_.title)
        );
        setPagesWhere(criteriaQuery, cb, employeeRoot, empJoin, empDepartmentJoin, titleJoin, searchBy);
        setOrder(criteriaQuery, cb, employeeRoot, empDepartmentJoin, titleJoin, orderBy, orderByDir);
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
        final CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<EmployeeDto> criteriaQuery = cb.createQuery(EmployeeDto.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin = employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin = empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Salary> employeeSalaryJoin = employeeRoot.join(Employee_.salaries, JoinType.INNER);
        Join<Employee, Title> titleJoin = employeeRoot.join(Employee_.titles, JoinType.INNER);
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
        ).where(cb.equal(employeeSalaryJoin.get(Salary_.toDate),
                        DATE_FAKE_END),
                cb.equal(titleJoin.get(Title_.toDate),
                        DATE_FAKE_END), cb.equal(empJoin.get(DeptEmp_.toDate), DATE_FAKE_END),
                cb.equal(employeeRoot.get(Employee_.employeeNumber), empNumber));
        TypedQuery<EmployeeDto> query = this.em.createQuery(criteriaQuery);
        return query.getResultStream().findFirst();
    }


    @Override
    public long findMaxEmployeeNumber() {
        final CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        criteriaQuery.select(cb.max(employeeRoot.get(Employee_.employeeNumber)));
        return this.em.createQuery(criteriaQuery).getSingleResult().longValue();
    }


    private int countEmp(final CriteriaBuilder cb, final Optional<String> searchLike) {
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin = employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin = empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Title> titleJoin = employeeRoot.join(Employee_.titles, JoinType.INNER);
        criteriaQuery.select(cb.count(employeeRoot.get(Employee_.employeeNumber)));
        setPagesWhere(criteriaQuery, cb, employeeRoot, empJoin, empDepartmentJoin, titleJoin, searchLike);
        return this.em.createQuery(criteriaQuery).getSingleResult().intValue();
    }


    private void setOrder(final CriteriaQuery<EmployeeListItemDto> cq,
                          final CriteriaBuilder cb, final Root<Employee> employeeRoot,
                          final Join<DeptEmp, Department> empDepartmentJoin,
                          final Join<Employee, Title> titleJoin,
                          final String orderBy, final String orderDir) {
        switch (orderBy) {
            case Employee_.EMPLOYEE_NUMBER: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    cq.orderBy(cb.asc(employeeRoot.get(Employee_.employeeNumber)));
                } else {
                    cq.orderBy(cb.desc(employeeRoot.get(Employee_.employeeNumber)));
                }
                break;
            }
            case "name": {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    cq.orderBy(cb.asc(employeeRoot.get(Employee_.lastName)), cb.asc(employeeRoot.get(Employee_.firstName)));
                } else {
                    cq.orderBy(cb.desc(employeeRoot.get(Employee_.lastName)), cb.desc(employeeRoot.get(Employee_.firstName)));
                }
                break;
            }
            case Employee_.HIRE_DATE: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    cq.orderBy(cb.asc(employeeRoot.get(Employee_.hireDate)));
                } else {
                    cq.orderBy(cb.desc(employeeRoot.get(Employee_.hireDate)));
                }
                break;
            }
            case Department_.DEPARTMENT_NAME: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    cq.orderBy(cb.asc(empDepartmentJoin.get(Department_.departmentNumber)));
                } else {
                    cq.orderBy(cb.desc(empDepartmentJoin.get(Department_.departmentNumber)));
                }
                break;
            }
            case TitlePk_.TITLE: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    cq.orderBy(cb.asc(titleJoin.get(Title_.titleId).get(TitlePk_.title)));
                } else {
                    cq.orderBy(cb.desc(titleJoin.get(Title_.titleId).get(TitlePk_.title)));
                }
                break;
            }
            default: {
                cq.orderBy(cb.asc(employeeRoot.get(Employee_.employeeNumber)));
            }
        }
    }


    private void setPagesWhere(final CriteriaQuery<?> cq,
                               final CriteriaBuilder cb,
                               final Root<Employee> employeeRoot,
                               final Join<Employee, DeptEmp> empJoin,
                               final Join<DeptEmp, Department> empDepartmentJoin,
                               final Join<Employee, Title> titleJoin,
                               final Optional<String> searchBy) {
        searchBy.ifPresentOrElse((searchByLikePresent) -> {
            Expression<String> convertDateInString = cb.function("DATE_FORMAT", String.class,
                    employeeRoot.get(Employee_.hireDate), cb.literal("%d-%m-%Y"));
            String searchLike = (searchByLikePresent + "%").toUpperCase();
            Predicate orPred = cb
                    .or(cb.like(employeeRoot.get(Employee_.employeeNumber).as(String.class), searchLike),
                    cb.like(cb.upper(employeeRoot.get(Employee_.firstName)), searchLike),
                    cb.like(cb.upper(employeeRoot.get(Employee_.lastName)), searchLike),
                    cb.like(convertDateInString, searchLike),
                    cb.like(cb.upper(empDepartmentJoin.get(Department_.departmentName)), searchLike),
                    cb.like(cb.upper(titleJoin.get(Title_.titleId).get(TitlePk_.title)), searchLike)
            );
            cq.where(cb.and(cb.equal(titleJoin.get(Title_.toDate),
                    DATE_FAKE_END), cb.equal(empJoin.get(DeptEmp_.toDate),
                    DATE_FAKE_END), orPred));

        }, () -> {
            cq.where(cb.equal(titleJoin.get(Title_.toDate),
                    DATE_FAKE_END), cb.equal(empJoin.get(DeptEmp_.toDate),
                    DATE_FAKE_END));
        });
    }
}