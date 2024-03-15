package com.employees.demo.controllers;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.dtos.ValidationResponseDto;
import com.employees.demo.services.EmployeeNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> employeeNotFoundExceptionHandler(EmployeeNotFoundException ex, WebRequest request) {
         ErrorResponseDto responseDto=new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                 HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
         return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
     }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        Collection<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        ValidationResponseDto validationResponseDto=new ValidationResponseDto(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors, LocalDateTime.now());
        return  new ResponseEntity<>(validationResponseDto,new HttpHeaders(), HttpStatus.BAD_REQUEST );
    }
}

