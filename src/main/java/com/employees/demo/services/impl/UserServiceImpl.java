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
import com.employees.demo.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(final AuthenticationManager authenticationManager,
                           final UserRepository userRepository,
                           final RoleRepository roleRepository,
                           final PasswordEncoder encoder,final JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void registerUser(final SignUpDto signUpRequest) {
        if(this.userRepository.existsByUsername(signUpRequest.getUsername())){
            throw new RuntimeException("Error: Username is already taken!");
        }
        if(this.userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new RuntimeException("Error: Email is already taken!");
        }
        final User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        String[] strRoles = signUpRequest.getRoles();
        Set<Role> roles=buildRoles(strRoles);
        user.setRoles(roles);
        this.userRepository.save(user);
    }

    @Override
    public JjwtResponse authenticateUser(final LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String[] roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toArray(String[]::new);

        String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername(), List.of(roles));
        return new JjwtResponse(jwt, loginRequest.getUsername(), userDetails.getEmail(), roles);
    }

    private Set<Role> buildRoles(final String[] strRoles){
        Set<Role> roles=new HashSet<>();
        if(strRoles.length==0){
         roles.add( this.roleRepository.findByDescription("USER")
                 .orElseThrow(()->new RuntimeException("Error, Role not found")));
        }
        final int len=strRoles.length;
        for (int i = 0; i < len; i++) {
            roles.add( this.roleRepository.findByDescription(strRoles[i])
                    .orElseThrow(()->new RuntimeException("Error, Role not found")));
        }
        return roles;
    }
}
