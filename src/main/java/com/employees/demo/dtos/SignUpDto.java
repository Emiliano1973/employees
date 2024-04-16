package com.employees.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;


public record SignUpDto(
        @NotBlank(message = "Username cannot be null or empty")
        @Size(min = 5, max = 15, message = "Username length cannot be shorter than 5  and not longer than 15")
        String username,
        @Email(message = "E-mail address is not valid", flags = Pattern.Flag.CASE_INSENSITIVE)
        @NotBlank(message = "E-mail address cannot be null or empty")
        String email,
        @NotBlank(message = "Password address cannot be null or empty")
        @Size(min = 8, message = "Password length cannot be less than 8")
        String password,
        String[] roles
)


        implements Serializable {
    public SignUpDto(@NotBlank(message = "Username cannot be null or empty")
                     @Size(min = 5, max = 15, message = "Username length cannot be shorter than 5  and not longer than 15")
                     String username, @Email(message = "E-mail address is not valid", flags = Pattern.Flag.CASE_INSENSITIVE)
                     @NotBlank(message = "E-mail address cannot be null or empty")
                     String email, @NotBlank(message = "Password address cannot be null or empty")
                     @Size(min = 8, message = "Password length cannot be less than 8")
                     String password, String[] roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        String[] rolesTmp = new String[roles.length];
        System.arraycopy(roles, 0, rolesTmp, 0, roles.length);
        this.roles = rolesTmp;
    }

    @Override
    public String[] roles() {
        String[] rolesTmp = new String[roles.length];
        System.arraycopy(roles, 0, rolesTmp, 0, roles.length);
        return rolesTmp;
    }

    @Override
    public boolean equals(final Object o) {
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
