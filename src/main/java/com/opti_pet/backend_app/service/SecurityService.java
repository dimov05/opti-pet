package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final UserService userService;

    public SecurityService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public boolean hasAdministratorAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.getAuthorities() != null
                && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().startsWith("ADMINISTRATOR_"));
    }

    public boolean userIdEqualsAuthenticatedUser(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }
        return userService.getUserByEmailOrThrowException(authentication.getName())
                .getId().toString().equals(userId);
    }
}
