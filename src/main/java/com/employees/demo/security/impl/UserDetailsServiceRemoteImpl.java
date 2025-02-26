package com.employees.demo.security.impl;

import com.employees.demo.dtos.ErrorResponseDto;
import com.employees.demo.dtos.UserResponseDto;
import com.employees.demo.utils.ServiceLocator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Optional;

import static com.employees.demo.security.impl.UserDetailsImpl.buildDetails;

public class UserDetailsServiceRemoteImpl implements UserDetailsService {

    private final ServiceLocator serviceLocator;

    public UserDetailsServiceRemoteImpl(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponseDto user=null;
        try {
            final String serviceUrl=getServiceUrl();
                user =
                    WebClient.create(serviceUrl).get()
                            .uri("/users/%s".formatted(username))
                            .retrieve()
                            .bodyToMono(UserResponseDto.class)
                            .block();
        } catch (WebClientResponseException e) {
            ErrorResponseDto errorResponse = Optional
                    .ofNullable(e.getResponseBodyAs(ErrorResponseDto.class)).orElseThrow(() -> new RuntimeException(e.getMessage(), e));
            throwProperException(errorResponse);
        }
        return buildDetails(user.username(), user.email(), user.password(), Arrays.asList(user.roles()));
        }

    private void throwProperException(ErrorResponseDto errorResponse) {
        HttpStatus status = HttpStatus.valueOf(errorResponse.statusCode());
        String messages = String.join(", ", errorResponse.messages());
        if (status == HttpStatus.NOT_FOUND) {
            throw new UsernameNotFoundException(messages);
        }
        throw new RuntimeException(messages);
    }

    private String getServiceUrl() {
        return serviceLocator.getUrlByServiceName("users-service");
    }
}


