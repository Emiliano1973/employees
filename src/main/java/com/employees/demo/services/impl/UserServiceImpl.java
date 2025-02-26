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
import com.employees.demo.services.UserAlreadyAddedException;
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
import java.util.Objects;
import java.util.Set;

import static jakarta.transaction.Transactional.TxType;

@Service("UserService")
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(final AuthenticationManager authenticationManager, final PasswordEncoder encoder,
                           final UserRepository userRepository, final RoleRepository roleRepository,
                           final JwtUtils jwtUtils) {

        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(TxType.REQUIRES_NEW)
    @Override
    public void registerUser(final SignUpDto signUpRequest) {
        if (this.userRepository.existsByUsername(signUpRequest.username())) {
            throw new UserAlreadyAddedException("Error: Username is already taken!");
        }
        if (this.userRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadyAddedException("Error: Email is already taken!");
        }
        final User user = new User(signUpRequest.username(),
                signUpRequest.email(),
                this.encoder.encode(signUpRequest.password()));
        String[] strRoles = signUpRequest.roles();
        Set<Role> roles = buildRoles(strRoles);
        user.setRoles(roles);
        this.userRepository.save(user);
    }

    @Override
    @Transactional(TxType.NOT_SUPPORTED)
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

    private Set<Role> buildRoles(final String[] strRoles) {
        Set<Role> roles = new HashSet<>();
        if (Objects.isNull(strRoles) || strRoles.length == 0) {
            String[] rolesUser = new String[1];
            rolesUser[0] = "USER";
            roles.addAll(this.roleRepository.findByDescriptionIn(rolesUser));
        } else {
            roles.addAll(this.roleRepository.findByDescriptionIn(strRoles));
        }
        return roles;
    }
}
