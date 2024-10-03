package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.UserChangePasswordRequest;
import com.opti_pet.backend_app.rest.request.UserEditProfileRequest;
import com.opti_pet.backend_app.rest.request.UserRegisterRequest;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.registerUser(userRegisterRequest);
    }

    @PutMapping("/{userId}/change-password")
    @PreAuthorize("@securityService.userIdEqualsAuthenticatedUser(#userId) || @securityService.hasAdministratorAuthority()")
    public UserResponse changePassword(@PathVariable(name = "userId") String userId, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        return userService.changePassword(userId, userChangePasswordRequest);
    }

    @PutMapping("/{userId}/edit-profile")
    @PreAuthorize("@securityService.userIdEqualsAuthenticatedUser(#userId) || @securityService.hasAdministratorAuthority()")
    public UserResponse editProfile(@PathVariable(name = "userId") String userId, @RequestBody UserEditProfileRequest userEditProfileRequest) {
        return userService.editProfile(userId, userEditProfileRequest);
    }

    @GetMapping("/me")
    public UserResponse getMyInformation() {
        return userService.getMyInformation();
    }
}
