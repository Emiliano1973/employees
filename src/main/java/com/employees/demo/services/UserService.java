package com.employees.demo.services;

import com.employees.demo.dtos.JjwtResponse;
import com.employees.demo.dtos.LoginRequestDto;
import com.employees.demo.dtos.SignUpDto;

public interface UserService {


    void registerUser(SignUpDto signUpRequest);

    JjwtResponse authenticateUser(LoginRequestDto loginRequest);


}
