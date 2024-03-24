package com.employees.demo.security;

import java.util.Collection;

public interface JwtUtils {

    String generateJwtToken(String username);
    String generateJwtToken(String username, Collection<String> roles);
    String getUserNameFromJwtToken(String token);

    boolean validateJwtToken(String authToken);

}
