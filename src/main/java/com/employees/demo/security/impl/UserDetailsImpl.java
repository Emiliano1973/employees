package com.employees.demo.security.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public final class UserDetailsImpl implements UserDetails {


    private final String username;

    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;


    private UserDetailsImpl(final String username, final String email, final String password,
                            final Collection<String> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public static UserDetailsImpl buildDetails( final String username,
                                               final String email, final String password,
                                               final Collection<String> authorities) {
        validateString(username, "Username");
        validateString(email, "Email");
        validateString(password, "Password");
        if (isEmpty(authorities)) {
            throw new IllegalArgumentException("Authorities cannot be null or empty");
        }
        return new UserDetailsImpl(username, email, password, authorities);
    }

    private static void validateString(String value, String fieldName) {
        if (Objects.isNull(value) || value.trim().equals("")) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.copyOf(this.authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
