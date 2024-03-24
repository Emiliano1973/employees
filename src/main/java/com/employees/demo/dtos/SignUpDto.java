package com.employees.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class SignUpDto implements Serializable {
    @NotBlank
    private  String username;
    @Email
    @NotBlank
    private  String email;
    @NotBlank
    private  String password;
    @NotNull
    private  String[] roles;
}
