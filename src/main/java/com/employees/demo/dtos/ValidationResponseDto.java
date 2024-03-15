package com.employees.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public record ValidationResponseDto(Integer statusCode, String error,
                                    Collection<String> messages,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
                                    LocalDateTime timestamp) {
    public ValidationResponseDto(Integer statusCode, String error,
                                 Collection<String> messages, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
    LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.error = error;
        this.messages =new ArrayList<>(messages);
        this.timestamp = timestamp;
    }

    @Override
    public Collection<String> messages() {
        return new ArrayList<>(messages);
    }
}
