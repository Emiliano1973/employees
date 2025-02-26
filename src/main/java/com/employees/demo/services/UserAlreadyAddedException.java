package com.employees.demo.services;

public class UserAlreadyAddedException extends RuntimeException {

    public UserAlreadyAddedException(String  message) {
        super(message);
    }

}
