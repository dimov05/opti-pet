package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String roleToSearch = "ADMINISTRATOR";

            List<String> roles = jwtAuthenticationToken.getToken().getClaim("roles");
            return roles.stream()
                    .anyMatch(role -> role.startsWith(roleToSearch));
        }
        return false;
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
