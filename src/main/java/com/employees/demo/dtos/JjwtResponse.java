package com.employees.demo.dtos;

public record JjwtResponse(String token, String username, String email, String[] roles) {
    public JjwtResponse(String token,String username, String email, String[] roles) {
        this.token=token ;
        this.username = username;
        this.email = email;
        String[] rolesTmp=new String[roles.length];
        System.arraycopy(roles, 0, rolesTmp, 0, roles.length);
        this.roles = rolesTmp;
    }

    @Override
    public String[] roles() {
        String[] rolesTmp=new String[roles.length];
        System.arraycopy(roles, 0, rolesTmp, 0, roles.length);
        return rolesTmp;
    }
}
