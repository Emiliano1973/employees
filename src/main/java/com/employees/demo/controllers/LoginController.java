package com.employees.demo.controllers;

import com.employees.demo.dtos.JjwtResponse;
import com.employees.demo.dtos.LoginRequestDto;
import com.employees.demo.dtos.SignUpDto;
import com.employees.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final UserService userService;

    public LoginController(@Qualifier("UserServiceRemote") final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody final LoginRequestDto loginRequest) {
        JjwtResponse response = this.userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/api/services/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@Valid @RequestBody final SignUpDto signUpRequest) {
        this.userService.registerUser(signUpRequest);
        Map<String, String> messages = new HashMap<>(1);
        messages.put("message", "User " + signUpRequest.username() + " is added");
        return ResponseEntity.status(HttpStatus.CREATED).body(messages);
    }
}
