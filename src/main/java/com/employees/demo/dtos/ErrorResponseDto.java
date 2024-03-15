package com.employees.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ErrorResponseDto(Integer statusCode, String error, String message,
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
                               LocalDateTime timestamp) implements Serializable {
}
