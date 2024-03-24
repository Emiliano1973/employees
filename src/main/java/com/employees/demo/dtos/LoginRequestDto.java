package com.employees.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class LoginRequestDto implements Serializable {

    private String username;
    private String password;
}
