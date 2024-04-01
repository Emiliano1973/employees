package com.employees.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpDto signUpDto = (SignUpDto) o;
        return Objects.equals(username, signUpDto.username)
                && Objects.equals(email, signUpDto.email)
                && Objects.equals(password, signUpDto.password) && Arrays.equals(roles, signUpDto.roles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username, email, password);
        result = 31 * result + Arrays.hashCode(roles);
        return result;
    }
}
