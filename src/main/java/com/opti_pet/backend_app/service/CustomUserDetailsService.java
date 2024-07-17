package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.persistence.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userService.getUserByEmailOrThrowException(email);
    }
}
