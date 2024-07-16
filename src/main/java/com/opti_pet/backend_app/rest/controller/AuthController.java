package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.UserLoginRequest;
import com.opti_pet.backend_app.rest.request.UserRegisterRequest;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.service.JwtService;
import com.opti_pet.backend_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.registerUser(userRegisterRequest);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody UserLoginRequest userLoginRequest) {
        return jwtService.generateToken(userLoginRequest);
    }
}
