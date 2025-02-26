package com.employees.demo.controllers;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.dtos.ErrorResponseDtoBuilder;
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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    private static final Log logger = LogFactory.getLog(ControllerExceptionAdvice.class);

    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDto> employeeNotFoundExceptionHandler(final EmployeeNotFoundException ex,
                                                                             final WebRequest request) {
        ErrorResponseDto responseDto =
                new ErrorResponseDtoBuilder()
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setError(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .setMessages(List.of(ex.getMessage())).build();
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDto> GlobalExceptionHandler(final Exception ex,
                                                                   final WebRequest request) {
        ErrorResponseDto responseDto = new ErrorResponseDtoBuilder()
                .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .setMessages(List.of("Internal error, contact administrator")).build();
        logger.error("Error message :" + ex.getMessage(), ex);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponseDto> AuthenticationExceptionHandler(final AuthenticationException ex,
                                                                           final WebRequest request) {
        ErrorResponseDto responseDto =new ErrorResponseDtoBuilder()
                .setStatusCode(HttpStatus.UNAUTHORIZED.value())
                .setError(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .setMessages(List.of(ex.getMessage())).build();
        return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(final MethodArgumentNotValidException ex) {
        Collection<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        ErrorResponseDto validationResponseDto =
                new ErrorResponseDtoBuilder()
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setError(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .setMessages(errors).build();
        return new ResponseEntity<>(validationResponseDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}

