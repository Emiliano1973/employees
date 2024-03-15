package com.employees.demo.controllers;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.services.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> employeeNotFoundExceptionHandler(EmployeeNotFoundException ex, WebRequest request) {
         ErrorResponseDto responseDto=new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                 HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
         return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
     }
}
