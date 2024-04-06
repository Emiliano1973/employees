package com.employees.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

public record LoginRequestDto(

    @NotBlank(message = "Username cannot be null or empty")
    @Size(min= 5, max=15, message = "Username length cannot be shorter than 5  and not longer than 15")
    String username,
    @NotBlank(message = "Password cannot be null or empty")
    @Size(min = 8, message = "Password length cannot be less than 8 ")
    String password)

    implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequestDto that = (LoginRequestDto) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
