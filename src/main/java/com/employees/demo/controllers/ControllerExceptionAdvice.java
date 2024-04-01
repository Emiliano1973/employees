package com.employees.demo.controllers;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.dtos.ValidationResponseDto;
import com.employees.demo.services.EmployeeNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    private static final Log logger = LogFactory.getLog(ControllerExceptionAdvice.class);

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> employeeNotFoundExceptionHandler(final EmployeeNotFoundException ex,
                                                                             final WebRequest request) {
         ErrorResponseDto responseDto=new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),
                 HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
         return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
     }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> GlobalExceptionHandler(final Exception ex,
                                                                   final WebRequest request) {
        ErrorResponseDto responseDto=new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Internal error, contact administrator", LocalDateTime.now());
          logger.error("Error message :"+ ex.getMessage(), ex);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDto> AuthenticationExceptionHandler(final AuthenticationException ex,
                                                                   final WebRequest request) {
        ErrorResponseDto responseDto=new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationResponseDto> handleValidationErrors(final MethodArgumentNotValidException ex) {
        String[] errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toArray(String[]::new);
        ValidationResponseDto validationResponseDto=new ValidationResponseDto(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), errors, LocalDateTime.now());
        return  new ResponseEntity<>(validationResponseDto,new HttpHeaders(), HttpStatus.BAD_REQUEST );
    }

}

