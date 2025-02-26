package com.employees.demo.dtos;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class ErrorResponseDtoBuilder {

    private Integer statusCode;
    private String error;
    private Collection<String> messages;


    public ErrorResponseDtoBuilder setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ErrorResponseDtoBuilder setError(String error) {
        this.error = error;
        return this;
    }

    public ErrorResponseDtoBuilder setMessages(Collection<String> messages) {
        this.messages = messages;
        return this;
    }

    public ErrorResponseDto build(){
        return new ErrorResponseDto(this.statusCode,
                this.error, List.copyOf(this.messages), LocalDateTime.now());
    }
}
