package com.employees.demo.services.impl;


import com.employees.demo.dao.repositories.RoleRepository;
import com.employees.demo.dao.repositories.UserRepository;
import com.employees.demo.dtos.JjwtResponse;
import com.employees.demo.dtos.LoginRequestDto;
import com.employees.demo.dtos.SignUpDto;
import com.employees.demo.entities.Role;
import com.employees.demo.entities.User;
import com.employees.demo.security.JwtUtils;
import com.employees.demo.security.impl.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class}) // or @SpringBootTest
@SpringBootTest
public class UserServiceImplTest {

    private static final String TEST_USERNAME="username";
    private static final String TEST_PASSWORD="password";

    private static final String TEST_EMAIL="test@test.it";

    private static final String[] TEST_ROLES=new String[]{"USER", "ADMIN"};

    private static final Collection<? extends GrantedAuthority> TEST_ROLES_AUTHORITIES=new ArrayList<>(List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority( "ADMIN")));
    private static final String TEST_ENCODED_PASSWORD="encodedPassword";

    private static final String TEST_TOKEN="token";


    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private  AuthenticationManager authenticationManager;
    @MockBean
    private  PasswordEncoder encoder;
    @MockBean
    private  UserRepository userRepository;
    @MockBean
    private  RoleRepository roleRepository;

   // @MockBean
   // private UserDetailsImpl userDetails;
    @MockBean
    private  JwtUtils jwtUtils;
    @MockBean
    private SignUpDto signUpRequest;

    @MockBean
    private LoginRequestDto loginRequestDto;

    @MockBean
    private Authentication authentication;

    @MockBean
    private Role role;

    @Test
    public void whenTheSignUpIsValidThenItShouldBeStored() throws Exception{
        buildSignUpDto();
        when(this.userRepository.existsByEmail(TEST_EMAIL)).thenReturn(Boolean.FALSE);
        when(this.userRepository.existsByUsername(TEST_USERNAME)).thenReturn(Boolean.FALSE);
        when(this.encoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
        when(this.roleRepository.findByDescription(any(String.class))).thenReturn(Optional.of(role));

        this.userService.registerUser(signUpRequest);

        verify(this.userRepository).save(any(User.class));
    }


    @Test
    public void whenTheRolesAreEmptyThenItShouldBeAddedUserDefaultStored() throws Exception {
        buildSignUpDto();
        when(this.signUpRequest.getRoles()).thenReturn(new String[0]);
        when(this.userRepository.existsByEmail(TEST_EMAIL)).thenReturn(Boolean.FALSE);
        when(this.userRepository.existsByUsername(TEST_USERNAME)).thenReturn(Boolean.FALSE);
        when(this.encoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
        when(this.roleRepository.findByDescription("USER")).thenReturn(Optional.of(role));

        this.userService.registerUser(signUpRequest);

        verify(this.roleRepository).findByDescription("USER");
        verify(this.userRepository).save(any(User.class));
    }
    @Test
    public void whenUsernameIsAlreadyTakenThenItShouldThrowException() throws Exception {
        buildSignUpDto();
        when(this.userRepository.existsByUsername(TEST_USERNAME)).thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.userService.registerUser(this.signUpRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
        verify(this.userRepository).existsByUsername(TEST_USERNAME);
        verify(this.userRepository, never()).existsByEmail(TEST_EMAIL);
        verify(this.encoder, never()).encode(TEST_PASSWORD);
        verify(this.roleRepository, never()).findByDescription(any(String.class));
        verify(this.userRepository, never()).save(any(User.class));
    }


    @Test
    public void whenUserNotFoundInDbsAlreadyTakenThenItShouldThrowException() throws Exception {
        buildSignUpDto();
        when(this.userRepository.existsByEmail(TEST_EMAIL)).thenReturn(Boolean.FALSE);
        when(this.userRepository.existsByUsername(TEST_USERNAME)).thenReturn(Boolean.FALSE);
        when(this.encoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
        when(this.roleRepository.findByDescription(any(String.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.userService.registerUser(this.signUpRequest);
        });

        assertEquals("Error, Role not found", exception.getMessage());
        verify(this.userRepository).existsByUsername(TEST_USERNAME);
        verify(this.userRepository).existsByEmail(TEST_EMAIL);
        verify(this.encoder).encode(TEST_PASSWORD);
        verify(this.roleRepository).findByDescription(any(String.class));
        verify(this.userRepository, never()).save(any(User.class));
    }



    @Test
    public void whenEmailIsAlreadyTakenThenItShouldThrowException() throws Exception {
        buildSignUpDto();
        when(this.userRepository.existsByEmail(TEST_EMAIL)).thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.userService.registerUser(this.signUpRequest);
        });

        assertEquals("Error: Email is already taken!", exception.getMessage());
        verify(this.userRepository).existsByUsername(TEST_USERNAME);
        verify(this.encoder, never()).encode(TEST_PASSWORD);
        verify(this.roleRepository, never()).findByDescription(any(String.class));
        verify(this.userRepository, never()).save(any(User.class));

    }

    @Test
    public void whenTheCredentialsAreCorrectThenUserIAuthenticateANdReturnJWSResponse() throws Exception {
        when(this.loginRequestDto.getUsername()).thenReturn(TEST_USERNAME);
        when(this.loginRequestDto.getPassword()).thenReturn(TEST_PASSWORD);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(TEST_USERNAME, TEST_PASSWORD))).thenReturn(this.authentication);
        final UserDetails userDetails = getUserDetails();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(TEST_USERNAME, TEST_ROLES )).thenReturn(TEST_TOKEN);

        JjwtResponse response=this.userService.authenticateUser(this.loginRequestDto);

        assertNotNull(response);
        assertEquals(TEST_USERNAME,response.username());
        assertEquals(TEST_EMAIL, response.email());
        assertEquals(TEST_TOKEN, response.token());
    }

    private void buildSignUpDto(){
        when(this.signUpRequest.getUsername()).thenReturn(TEST_USERNAME);
        when(this.signUpRequest.getEmail()).thenReturn(TEST_EMAIL);
        when(this.signUpRequest.getPassword()).thenReturn(TEST_PASSWORD);
        when(this.signUpRequest.getRoles()).thenReturn(TEST_ROLES);
    }

    private UserDetails getUserDetails(){
     return  UserDetailsImpl.buildDetails(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD,
             List.of(TEST_ROLES));
    }

}


