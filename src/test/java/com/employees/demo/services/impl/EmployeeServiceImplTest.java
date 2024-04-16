package com.employees.demo.services.impl;


import com.employees.demo.dao.EmployeeDao;
import com.employees.demo.dao.repositories.DeptEmpRepository;
import com.employees.demo.dao.repositories.EmployeeRepository;
import com.employees.demo.dao.repositories.SalaryRepository;
import com.employees.demo.dao.repositories.TitleRepository;
import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.EmployeeListItemDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.dtos.PaginatorDtoBuilder;
import com.employees.demo.entities.DeptEmp;
import com.employees.demo.entities.Employee;
import com.employees.demo.entities.Salary;
import com.employees.demo.entities.Title;
import com.employees.demo.entities.pk.EmpDeptsPk;
import com.employees.demo.entities.pk.SalaryPk;
import com.employees.demo.entities.pk.TitlePk;
import com.employees.demo.services.EmployeeNotFoundException;
import com.employees.demo.utils.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class}) // or @SpringBootTest
@SpringBootTest
public class EmployeeServiceImplTest {
    private static final Long EMP_NUMBER = Long.valueOf(1001);
    private static final Integer PAGE_NUMBER = Integer.valueOf(1);
    private static final Integer PAGE_SIZE = Integer.valueOf(10);
    private static final String TITLE = "title";
    private static final String TITLE_2 = "title2";

    private static final String DEPARTMENT = "dept1";
    private static final String DEPARTMENT_2 = "dept2";

    private static final String ORDER_BY_TEST = "employeeNumber";

    private static final String ORDER_BY_DIR_TEST = "ASC";


    private static final Integer SALARY = Integer.valueOf(3333333);
    private static final Integer SALARY_2 = Integer.valueOf(4444444);
    private static final LocalDate END_VALID_DATE = LocalDate.of(9999, 1, 1);


    @Autowired
    private EmployeeServiceImpl employeeService;

    @MockBean
    private EmployeeDao employeeDao;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private DeptEmpRepository deptEmpRepository;
    @MockBean
    private SalaryRepository salaryRepository;
    @MockBean
    private TitleRepository titleRepository;
    @MockBean
    private Employee employee;
    @MockBean
    private DeptEmp deptEmp;
    @MockBean
    private EmpDeptsPk empDeptsPk;

    @MockBean
    private Title title;
    @MockBean
    private TitlePk titlePk;
    @MockBean
    private Salary salary;
    @MockBean
    private SalaryPk salaryPk;

    @Test
    public void whenBrowsePageThePageShouldReturn() throws Exception {
        Optional<String> empty = Optional.empty();
        when(this.employeeDao.findPages(PAGE_NUMBER, PAGE_SIZE, ORDER_BY_TEST, ORDER_BY_DIR_TEST, empty)).thenReturn(getPaginationDtoWithList());
        PaginationDto paginationDto = this.employeeService.findByPage(PAGE_NUMBER, PAGE_SIZE, ORDER_BY_TEST, ORDER_BY_DIR_TEST, empty);
        assertNotNull(paginationDto);
        assertEquals(PAGE_NUMBER, paginationDto.currentPage());
        assertEquals(PAGE_SIZE, paginationDto.pageSize());
        assertEquals(1, paginationDto.totalElements());
    }


    @Test
    public void whenISendAEmpNumThenItShouldReturnAEmployeeData() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        when((this.employeeDao.findByEmpNumber(EMP_NUMBER))).thenReturn(Optional.of(employeeDto));
        Optional<EmployeeDto> employeeDtoOptional = this.employeeService.findByEmpNum(EMP_NUMBER);
        assertNotNull(employeeDtoOptional);
        assertFalse(employeeDtoOptional.isEmpty());
        EmployeeDto employeeDtoOut = employeeDtoOptional.get();
        assertEquals(EMP_NUMBER, employeeDtoOut.employeeNumber());
    }

    @Test
    public void aNewEmployeeShouldBeInsertedInDatabase() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        when(employeeDao.findMaxEmployeeNumber()).thenReturn(EMP_NUMBER);
        this.employeeService.insertNewEmployee(employeeDto);
        verify(this.employeeRepository).save(any(Employee.class));
        verify(this.deptEmpRepository).save(any(DeptEmp.class));
        verify(this.salaryRepository).save(any(Salary.class));
        verify(this.titleRepository).save(any(Title.class));

    }

    @Test
    public void anEmployeeShouldBeUpdatedInDatabase() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        when(this.employeeRepository.findById(EMP_NUMBER)).thenReturn(Optional.of(this.employee));
        when(this.employee.getEmployeeNumber()).thenReturn(EMP_NUMBER);

        when(this.deptEmp.getEmpDeptsId()).thenReturn(this.empDeptsPk);
        when(this.empDeptsPk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(this.deptEmp.getToDate()).thenReturn(END_VALID_DATE);
        when((this.empDeptsPk.getDepartmentNumber())).thenReturn(DEPARTMENT_2);

        Set<DeptEmp> deptEmpsSet = Set.of(deptEmp);
        when(employee.getDepartments()).thenReturn(deptEmpsSet);
        when(this.titlePk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(this.titlePk.getTitle()).thenReturn(TITLE_2);
        when(this.title.getTitleId()).thenReturn(titlePk);
        when(this.title.getToDate()).thenReturn(END_VALID_DATE);
        Set<Title> titles = Set.of(this.title);
        when(employee.getTitles()).thenReturn(titles);

        when(salaryPk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(salary.getSalaryId()).thenReturn(salaryPk);
        when(salary.getSalary()).thenReturn(SALARY_2);
        when(salary.getToDate()).thenReturn(END_VALID_DATE);
        Set<Salary> salaries = Set.of(salary);
        when(employee.getSalaries()).thenReturn(salaries);

        this.employeeService.updateEmployee(EMP_NUMBER, employeeDto);

        verify(this.employee).setFirstName(employeeDto.firstName());
        verify(this.employee).setLastName(employeeDto.lastName());
        verify(this.employee).setGender(employeeDto.gender());
        verify(this.employee).setBirthDate(employeeDto.birthDate());
        verify(this.employee).setHireDate(employeeDto.hireDate());
        verify(this.employee).setHireDate(employeeDto.hireDate());
        verify(this.employeeRepository).save(any(Employee.class));
        verify(this.deptEmpRepository).save(any(DeptEmp.class));
        verify(this.salaryRepository).save(any(Salary.class));
        verify(this.titleRepository).save(any(Title.class));
        verify(this.deptEmp).setToDate(any(LocalDate.class));
        verify(this.title).setToDate(any(LocalDate.class));
        verify(this.salary).setToDate(any(LocalDate.class));
    }

    @Test
    public void anEmployeeShouldBeUpdatedAndSalaryDeptAndTitleNotChangedInDatabase() throws Exception {
        EmployeeDto employeeDto = getEmployeeDto();
        when(this.employeeRepository.findById(EMP_NUMBER)).thenReturn(Optional.of(this.employee));
        when(this.employee.getEmployeeNumber()).thenReturn(EMP_NUMBER);

        when(this.deptEmp.getEmpDeptsId()).thenReturn(this.empDeptsPk);
        when(this.empDeptsPk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(this.deptEmp.getToDate()).thenReturn(END_VALID_DATE);
        when((this.empDeptsPk.getDepartmentNumber())).thenReturn(DEPARTMENT);

        Set<DeptEmp> deptEmpsSet = Set.of(deptEmp);
        when(employee.getDepartments()).thenReturn(deptEmpsSet);

        when(this.titlePk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(this.titlePk.getTitle()).thenReturn(TITLE);
        when(this.title.getTitleId()).thenReturn(titlePk);
        when(this.title.getToDate()).thenReturn(END_VALID_DATE);
        Set<Title> titles = Set.of(this.title);
        when(employee.getTitles()).thenReturn(titles);

        when(salaryPk.getEmployeeNumber()).thenReturn(EMP_NUMBER);
        when(salary.getSalaryId()).thenReturn(salaryPk);
        when(salary.getSalary()).thenReturn(SALARY);
        when(salary.getToDate()).thenReturn(END_VALID_DATE);
        Set<Salary> salaries = Set.of(salary);
        when(employee.getSalaries()).thenReturn(salaries);

        this.employeeService.updateEmployee(EMP_NUMBER, employeeDto);

        verify(this.employee).setFirstName(employeeDto.firstName());
        verify(this.employee).setLastName(employeeDto.lastName());
        verify(this.employee).setGender(employeeDto.gender());
        verify(this.employee).setBirthDate(employeeDto.birthDate());
        verify(this.employee).setHireDate(employeeDto.hireDate());
        verify(this.employee).setHireDate(employeeDto.hireDate());
        verify(this.employeeRepository).save(any(Employee.class));
        verify(this.deptEmpRepository, never()).save(any(DeptEmp.class));
        verify(this.salaryRepository, never()).save(any(Salary.class));
        verify(this.titleRepository, never()).save(any(Title.class));
        verify(this.deptEmp, never()).setToDate(any(LocalDate.class));
        verify(this.title, never()).setToDate(any(LocalDate.class));
        verify(this.salary, never()).setToDate(any(LocalDate.class));
    }

    @Test
    public void ShouldInExceptionIfEmployeeNotFoundForEmpNumber() throws Exception {
        final String message = "Employee not found for [" + EMP_NUMBER + "] emp number";
        EmployeeDto employeeDto = getEmployeeDto();
        when(this.employeeRepository.findById(EMP_NUMBER)).thenReturn(Optional.empty());

        EmployeeNotFoundException ex = assertThrows(EmployeeNotFoundException.class,
                () -> this.employeeService.updateEmployee(EMP_NUMBER, employeeDto));

        assertEquals(message, ex.getMessage());
        verify(this.employee, never()).setFirstName(employeeDto.firstName());
        verify(this.employee, never()).setLastName(employeeDto.lastName());
        verify(this.employee, never()).setGender(employeeDto.gender());
        verify(this.employee, never()).setBirthDate(employeeDto.birthDate());
        verify(this.employee, never()).setHireDate(employeeDto.hireDate());
        verify(this.employee, never()).setHireDate(employeeDto.hireDate());
        verify(this.employeeRepository, never()).save(any(Employee.class));
        verify(this.deptEmpRepository, never()).save(any(DeptEmp.class));
        verify(this.salaryRepository, never()).save(any(Salary.class));
        verify(this.titleRepository, never()).save(any(Title.class));
        verify(this.deptEmp, never()).setToDate(any(LocalDate.class));
        verify(this.title, never()).setToDate(any(LocalDate.class));
        verify(this.salary, never()).setToDate(any(LocalDate.class));
    }

    @Test
    public void deleteByEmployerNumber() {
        this.employeeService.deleteEmployee(EMP_NUMBER);
        verify(this.employeeRepository).deleteById(EMP_NUMBER);
    }

    private PaginationDto getPaginationDtoWithList() {
        return new PaginatorDtoBuilder().setCurrentPageTotalElements(1).setTotalPages(1)
                .setCurrentPage(PAGE_NUMBER).setTotalPages(1).setTotalPages(1).setTotalElements(1)
                .setPageSize(PAGE_SIZE).setElements(List.of(getEmployeeListItemDto()))
                .setTotalPages(1).createPaginatorDto();
    }

    private EmployeeListItemDto getEmployeeListItemDto() {
        return new EmployeeListItemDto(EMP_NUMBER, "Ciro", "Esposito", LocalDate.now(), DEPARTMENT, TITLE);
    }

    private EmployeeDto getEmployeeDto() {
        return new EmployeeDto(EMP_NUMBER, "Ciro", "Esposito", Gender.MALE, LocalDate.now(), LocalDate.now(), DEPARTMENT, SALARY, TITLE);
    }
}
