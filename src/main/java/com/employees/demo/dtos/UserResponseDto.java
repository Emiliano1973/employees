package com.employees.demo.dtos;

import java.util.Objects;

public record UserResponseDto(String username, String password, String email, String[] roles) {
    public UserResponseDto(String username, String  password, String email, String[] roles) {
        this.username = username;
        this.password=password;
        this.email = email;
        String[] rolesTmp = new String[roles.length];
        System.arraycopy(roles, 0, rolesTmp, 0, roles.length);
        this.roles = rolesTmp;
    }

    @Override
    public String[] roles() {
        String[] rolesTmp = new String[this.roles.length];
        System.arraycopy(this.roles, 0, rolesTmp, 0, this.roles.length);
        return rolesTmp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserResponseDto response = (UserResponseDto) o;
        return Objects.equals(username.toLowerCase(), response.username.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username.toLowerCase());
    }
}
