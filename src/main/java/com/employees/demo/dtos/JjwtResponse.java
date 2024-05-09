package com.employees.demo.dtos;

import java.util.Objects;

public record JjwtResponse(String token, String username, String email, String[] roles) {
    public JjwtResponse(String token, String username, String email, String[] roles) {
        this.token = token;
        this.username = username;
        this.email = email;
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
        final JjwtResponse response = (JjwtResponse) o;
        return Objects.equals(token, response.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
