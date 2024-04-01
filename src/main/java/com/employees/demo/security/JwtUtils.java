package com.employees.demo.security;

public interface JwtUtils {

    String generateJwtToken(String username);
    String generateJwtToken(String username, String[] roles);
    String getUserNameFromJwtToken(String token);

    boolean validateJwtToken(String authToken);

}
