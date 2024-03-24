package com.employees.demo.security.config;

import com.employees.demo.security.AuthEntryPointJwt;
import com.employees.demo.security.AuthTokenFilter;
import com.employees.demo.security.JwtUtils;
import com.employees.demo.security.impl.JwtUtilsImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
 jsr250Enabled = true, prePostEnabled = true) // by default
///@Profile("!test")
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final String jwtSecret;
    private final int jwtExpirationMs;
    public WebSecurityConfig(
            @Qualifier("customUserDetailsService") final UserDetailsService userDetailsService,
            @Value("${employees.app.jwtSecret}") final String jwtSecret,
            @Value("${employees.app.jwtExpirationMs}") final int jwtExpirationMs
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtSecret=jwtSecret;
        this.jwtExpirationMs=jwtExpirationMs;
    }

    public AuthenticationEntryPoint unauthorizedHandler(){
        return new AuthEntryPointJwt();
    }

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtilsImpl(jwtSecret, jwtExpirationMs);
    }

    @Bean
    public OncePerRequestFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils(), userDetailsService);
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler()))
                .authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**")
                        .permitAll().requestMatchers("/api/services/**").authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
