package com.employees.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

public record ErrorResponseDto(Integer statusCode, String error, Collection<String> messages,
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
                               LocalDateTime timestamp) implements Serializable {
}
