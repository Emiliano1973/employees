package com.employees.demo.dao.impl;

import com.employees.demo.dao.EmployeeDao;
import com.employees.demo.dtos.*;
import com.employees.demo.entities.*;
import com.employees.demo.entities.pk.TitlePk_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

    private static final LocalDate DATE_FAKE_END = LocalDate.of(9999, 1, 1);
    @PersistenceContext
    private EntityManager em;

    @Override
    public PaginationDto findPages(final PaginationRequestDto request) {
        final int page = request.page();
        final int pageSize = request.pageSize();
        final String orderBy = request.orderBy();
        final String orderByDir = request.orderByDir();
        final Optional<String> searchBy = request.searchLike();
        final CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        int countEmp = countEmp(criteriaBuilder, searchBy);
        if (countEmp == 0) {
            return new PaginatorDtoBuilder().setCurrentPage(page).setPageSize(pageSize).createEmptyPaginatorDto();
        }
        CriteriaQuery<EmployeeListItemDto> criteriaQuery = criteriaBuilder.createQuery(EmployeeListItemDto.class);
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
        setPagesWhere(criteriaQuery, criteriaBuilder, employeeRoot, empJoin, empDepartmentJoin, titleJoin, searchBy);
        setOrder(criteriaQuery, criteriaBuilder, employeeRoot, empDepartmentJoin, titleJoin, orderBy, orderByDir);
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
        final CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<EmployeeDto> criteriaQuery = criteriaBuilder.createQuery(EmployeeDto.class);
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
        ).where(criteriaBuilder.equal(employeeSalaryJoin.get(Salary_.toDate),
                        DATE_FAKE_END),
                criteriaBuilder.equal(titleJoin.get(Title_.toDate),
                        DATE_FAKE_END), criteriaBuilder.equal(empJoin.get(DeptEmp_.toDate), DATE_FAKE_END),
                criteriaBuilder.equal(employeeRoot.get(Employee_.employeeNumber), empNumber));
        TypedQuery<EmployeeDto> query = this.em.createQuery(criteriaQuery);
        return query.getResultStream().findFirst();
    }


    @Override
    public long findMaxEmployeeNumber() {
        final CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        criteriaQuery.select(criteriaBuilder.max(employeeRoot.get(Employee_.employeeNumber)));
        return this.em.createQuery(criteriaQuery).getSingleResult().longValue();
    }


    private int countEmp(final CriteriaBuilder criteriaBuilder, final Optional<String> searchLike) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
        Join<Employee, DeptEmp> empJoin = employeeRoot.join(Employee_.departments, JoinType.INNER);
        Join<DeptEmp, Department> empDepartmentJoin = empJoin.join(DeptEmp_.department, JoinType.INNER);
        Join<Employee, Title> titleJoin = employeeRoot.join(Employee_.titles, JoinType.INNER);
        criteriaQuery.select(criteriaBuilder.count(employeeRoot.get(Employee_.employeeNumber)));
        setPagesWhere(criteriaQuery, criteriaBuilder, employeeRoot, empJoin, empDepartmentJoin, titleJoin, searchLike);
        return this.em.createQuery(criteriaQuery).getSingleResult().intValue();
    }


    private void setOrder(final CriteriaQuery<EmployeeListItemDto> criteriaQuery,
                          final CriteriaBuilder criteriaBuilder,
                          final Root<Employee> employeeRoot,
                          final Join<DeptEmp, Department> empDepartmentJoin,
                          final Join<Employee, Title> titleJoin,
                          final String orderBy, final String orderDir) {
        switch (orderBy) {
            case Employee_.EMPLOYEE_NUMBER: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(Employee_.employeeNumber)));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(Employee_.employeeNumber)));
                }
                break;
            }
            case "name": {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(criteriaBuilder.upper(employeeRoot.get(Employee_.lastName))),
                            criteriaBuilder.asc(criteriaBuilder.upper(employeeRoot.get(Employee_.firstName))));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.upper(employeeRoot.get(Employee_.lastName))),
                            criteriaBuilder.desc(criteriaBuilder.upper(employeeRoot.get(Employee_.firstName))));
                }
                break;
            }
            case Employee_.HIRE_DATE: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(Employee_.hireDate)));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(Employee_.hireDate)));
                }
                break;
            }
            case Department_.DEPARTMENT_NAME: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(empDepartmentJoin.get(Department_.departmentNumber)));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(empDepartmentJoin.get(Department_.departmentNumber)));
                }
                break;
            }
            case TitlePk_.TITLE: {
                if ("ASC".equalsIgnoreCase(orderDir)) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(criteriaBuilder.upper(titleJoin.get(Title_.titleId).get(TitlePk_.title))));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.upper(titleJoin.get(Title_.titleId).get(TitlePk_.title))));
                }
                break;
            }
            default: {
                criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(Employee_.employeeNumber)));
            }
        }
    }


    private void setPagesWhere(final CriteriaQuery<?> criteriaQuery,
                               final CriteriaBuilder criteriaBuilder,
                               final Root<Employee> employeeRoot,
                               final Join<Employee, DeptEmp> empJoin,
                               final Join<DeptEmp, Department> empDepartmentJoin,
                               final Join<Employee, Title> titleJoin,
                               final Optional<String> searchBy) {
        searchBy.ifPresentOrElse((searchByLikePresent) -> {
            Expression<String> convertDateInString = criteriaBuilder.function("DATE_FORMAT", String.class,
                    employeeRoot.get(Employee_.hireDate), criteriaBuilder.literal("%d-%m-%Y"));
            String searchLike = (new StringBuilder().append(searchByLikePresent).append("%").toString()).toUpperCase();
            Predicate orPred = criteriaBuilder
                    .or(criteriaBuilder.like(employeeRoot.get(Employee_.employeeNumber).as(String.class), searchLike),
                            criteriaBuilder.like(criteriaBuilder.upper(employeeRoot.get(Employee_.firstName)), searchLike),
                            criteriaBuilder.like(criteriaBuilder.upper(employeeRoot.get(Employee_.lastName)), searchLike),
                            criteriaBuilder.like(convertDateInString, searchLike),
                            criteriaBuilder.like(criteriaBuilder.upper(empDepartmentJoin.get(Department_.departmentName)), searchLike),
                            criteriaBuilder.like(criteriaBuilder.upper(titleJoin.get(Title_.titleId).get(TitlePk_.title)), searchLike)
                    );
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(titleJoin.get(Title_.toDate),
                    DATE_FAKE_END), criteriaBuilder.equal(empJoin.get(DeptEmp_.toDate),
                    DATE_FAKE_END), orPred));
        }, () -> {
            criteriaQuery.where(criteriaBuilder.equal(titleJoin.get(Title_.toDate),
                    DATE_FAKE_END), criteriaBuilder.equal(empJoin.get(DeptEmp_.toDate),
                    DATE_FAKE_END));
        });
    }
}