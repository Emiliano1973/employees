package com.employees.demo.services.impl;

import com.employees.demo.dao.EmployeeDao;
import com.employees.demo.dao.repositories.*;
import com.employees.demo.dtos.EmployeeDto;
import com.employees.demo.dtos.PaginationDto;
import com.employees.demo.entities.DeptEmp;
import com.employees.demo.entities.Employee;
import com.employees.demo.entities.Salary;
import com.employees.demo.entities.Title;
import com.employees.demo.services.EmployeeNotFoundException;
import com.employees.demo.services.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static jakarta.transaction.Transactional.TxType;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final LocalDate END_VALID_DATE=LocalDate.of(9999, 1,1);

    private final EmployeeDao employeeDao;
    private final EmployeeRepository employeeRepository;

    private final DeptEmpRepository deptEmpRepository;

    private final SalaryRepository salaryRepository;

    private final TitleRepository titleRepository;

    public EmployeeServiceImpl(final EmployeeDao employeeDao,
                               final EmployeeRepository employeeRepository,
                               final DeptEmpRepository deptEmpRepository,
                               final SalaryRepository salaryRepository,
                               final TitleRepository titleRepository) {
        this.employeeDao = employeeDao;
        this.employeeRepository = employeeRepository;
        this.deptEmpRepository = deptEmpRepository;
        this.salaryRepository = salaryRepository;
        this.titleRepository=titleRepository;
    }

    @Override
    @Transactional(TxType.NOT_SUPPORTED)
    public PaginationDto findByPage(final int page,final int pageSize) {
        return this.employeeDao.findPages(page, pageSize);
    }

    @Override
    @Transactional(TxType.NOT_SUPPORTED)
    public Optional<EmployeeDto> findByEmpNum(final Long empNum) {
        return this.employeeDao.findByEmpNumber(empNum);
    }

    @Override
    @Transactional(TxType.REQUIRES_NEW)
    public void insertNewEmployee(final EmployeeDto employeeDto) {
        long newNUmber=this.employeeDao.findMaxEmployeeNumber()+1;
        Employee employee=new Employee();
        employee.setEmployeeNumber(newNUmber);
        fillEmployee(employeeDto, employee);
        this.employeeRepository.save(employee);
        String deptNumber=employeeDto.departmentNumber();
        DeptEmp deptEmp=new DeptEmp(newNUmber,deptNumber,
                employeeDto.hireDate(), END_VALID_DATE  );
        this.deptEmpRepository.save(deptEmp);
        Salary salary=new Salary(newNUmber, employeeDto.salary(), employeeDto.hireDate(),
                END_VALID_DATE );
        this.salaryRepository.save(salary);
        Title title=new Title(newNUmber, employeeDto.title(),employeeDto.hireDate(), END_VALID_DATE);
        this.titleRepository.save(title);
    }

    @Override
    @Transactional(TxType.REQUIRES_NEW)
    public void updateEmployee(final Long empNum,final EmployeeDto employeeDto) {
        Employee employee=this.employeeRepository.findById(empNum).orElseThrow(()-> new EmployeeNotFoundException(empNum));
        fillEmployee(employeeDto, employee);
        this.employeeRepository.save(employee);
        String departmentNUmber=employeeDto.departmentNumber();
        DeptEmp deptEmp= employee.getDepartments().stream()
                .filter(dp->dp.getToDate().isEqual(END_VALID_DATE)).findFirst().get();
        if(!deptEmp.getEmpDeptsId().getDepartmentNumber().equalsIgnoreCase(departmentNUmber)){
            deptEmp.setToDate(LocalDate.now());
            DeptEmp deptEmpNew=new DeptEmp(empNum, departmentNUmber, LocalDate.now(), END_VALID_DATE);
            this.deptEmpRepository.save(deptEmpNew);
        }

        Integer salaryNum=employeeDto.salary();
        Salary salary=employee.getSalaries().stream().filter(sal-> sal.getToDate().isEqual(END_VALID_DATE)).findFirst().get();
        if(salary.getSalary().intValue()!=salaryNum.intValue()){
            salary.setToDate(LocalDate.now());
            Salary newSalary=new Salary(empNum, salaryNum, LocalDate.now(), END_VALID_DATE);
            this.salaryRepository.save(newSalary);
        }

        String titleS=employeeDto.title();
        Title title=employee.getTitles().stream().filter(tit-> tit.getToDate().isEqual(END_VALID_DATE)).findFirst().get();
        if(!title.getTitleId().getTitle().equalsIgnoreCase(titleS)){
            title.setToDate(LocalDate.now());
            Title newTitle=new Title(empNum, titleS, LocalDate.now(), END_VALID_DATE);
            this.titleRepository.save(newTitle);
        }

    }


    @Override
    @Transactional(TxType.REQUIRES_NEW)
    public void deleteEmployee(final Long empNum) {
        this.deptEmpRepository.deleteByEmpDeptsIdEmployeeNumber(empNum);
        this.salaryRepository.deleteBySalaryIdEmployeeNumber(empNum);
        this.titleRepository.deleteByTitleIdEmployeeNumber(empNum);
        this.employeeRepository.deleteById(empNum);
    }

    private void fillEmployee(final EmployeeDto employeeDto, final Employee employee){
        employee.setFirstName(employeeDto.firstName());
        employee.setLastName(employeeDto.lastName());
        employee.setGender(employeeDto.gender());
        employee.setBirthDate(employeeDto.birthDate());
        employee.setHireDate(employeeDto.hireDate());
    }

}
