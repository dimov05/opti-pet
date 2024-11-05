package com.opti_pet.backend_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String extractEmailFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String[] claims = {"email", "preferred_username", "unique_name", "upn"};

            for (String claim : claims) {
                String email = jwtAuthenticationToken.getToken().getClaim(claim);
                if (email != null) {
                    return email;
                }
            }
            throw new IllegalArgumentException("Invalid token");
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
