package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.user.UserLoginRequest;
import com.opti_pet.backend_app.util.JwtServiceHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final JwtServiceHelper jwtServiceHelper;

    @PostMapping("/generateToken")
    public String generateToken(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return jwtServiceHelper.generateToken(userLoginRequest);
    }
}
