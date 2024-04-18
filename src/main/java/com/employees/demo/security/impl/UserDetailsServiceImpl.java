package com.employees.demo.security.impl;

import com.employees.demo.dao.repositories.UserRepository;
import com.employees.demo.entities.Role;
import com.employees.demo.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static jakarta.transaction.Transactional.*;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(TxType.NOT_SUPPORTED)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found for username: " + username));
        Collection<String> roles = user.getRoles().stream().map(Role::getDescription).toList();
        return UserDetailsImpl.buildDetails(user.getUsername(), user.getEmail(), user.getPassword(), roles);
    }
}
