package com.employees.demo.services.impl;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.dtos.JjwtResponse;
import com.employees.demo.dtos.LoginRequestDto;
import com.employees.demo.dtos.SignUpDto;
import com.employees.demo.security.JwtUtils;
import com.employees.demo.security.impl.UserDetailsImpl;
import com.employees.demo.services.UserAlreadyAddedException;
import com.employees.demo.services.UserService;
import com.employees.demo.utils.ServiceLocator;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service("UserServiceRemote")
public class UserServiceRemoteImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final ServiceLocator serviceLocator;;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public UserServiceRemoteImpl(final AuthenticationManager authenticationManager,
                                final ServiceLocator serviceLocator,
                                 final PasswordEncoder encoder,final JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.serviceLocator = serviceLocator;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void registerUser(final SignUpDto signUpRequest) {
        try {
            SignUpDto signUpReq = new SignUpDto(signUpRequest.username(), signUpRequest.email(),
                    this.encoder.encode(signUpRequest.password()), signUpRequest.roles());
            HttpStatusCode status =
                    WebClient.builder().baseUrl(getServiceUrl())
                            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .build().post()
                            .uri("/users").bodyValue(signUpReq)
                            .exchangeToMono(response -> Mono.just(response.statusCode())) // Otteniamo il codice di stato
                            .block();
        } catch (WebClientResponseException e) {
            ErrorResponseDto errorResponse = Optional
                    .ofNullable(e.getResponseBodyAs(ErrorResponseDto.class)).orElseThrow(() -> new RuntimeException(e.getMessage(), e));
            throwProperException(errorResponse);
        }

    }
    @Override
    public JjwtResponse authenticateUser(final LoginRequestDto loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String[] roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toArray(String[]::new);
        String jwt = this.jwtUtils.generateJwtToken(loginRequest.username(), roles);
        return new JjwtResponse(jwt, loginRequest.username(), userDetails.getEmail(), roles);
    }


    private void throwProperException(ErrorResponseDto errorResponse) {
        HttpStatus status = HttpStatus.valueOf(errorResponse.statusCode());
        String messages = String.join(", ", errorResponse.messages());
        switch (status){
            case NOT_FOUND -> throw new UsernameNotFoundException(messages);
            case CONFLICT -> throw new UserAlreadyAddedException(messages);
        }
        throw new RuntimeException(messages);
    }

    private String getServiceUrl() {
        return serviceLocator.getUrlByServiceName("users-service");
    }
}
