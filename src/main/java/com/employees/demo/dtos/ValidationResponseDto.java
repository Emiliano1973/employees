package com.employees.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ValidationResponseDto(Integer statusCode, String error,
                                    String[] messages,
                                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
                                    LocalDateTime timestamp) {
    public ValidationResponseDto(Integer statusCode, String error,
                                 String[] messages, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
                                 LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.error = error;
        String[] messagesTmp = new String[messages.length];
        System.arraycopy(messages, 0, messagesTmp, 0, messages.length);
        this.messages = messagesTmp;
        this.timestamp = timestamp;
    }

    @Override
    public String[] messages() {
        String[] messagesTmp = new String[messages.length];
        System.arraycopy(messages, 0, messagesTmp, 0, messages.length);
        return messagesTmp;
    }
}
