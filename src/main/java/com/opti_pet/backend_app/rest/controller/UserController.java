package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.*;
import com.opti_pet.backend_app.rest.response.RoleResponse;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.service.RoleService;
import com.opti_pet.backend_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.registerUser(userRegisterRequest);
    }

    @PostMapping("/admin/register")
    public UserResponse registerUserAsAdmin(@Valid @RequestBody UserRegisterAsAdminRequest userRegisterAsAdminRequest) {
        return userService.registerUserAsAdmin(userRegisterAsAdminRequest);
    }

    @PutMapping("/{userId}/change-password")
    @PreAuthorize("@securityService.userIdEqualsAuthenticatedUser(#userId) || @securityService.hasAdministratorAuthority()")
    public UserResponse changePassword(@PathVariable(name = "userId") String userId,@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        return userService.changePassword(userId, userChangePasswordRequest);
    }

    @PutMapping("/{userId}/edit-profile")
    @PreAuthorize("@securityService.userIdEqualsAuthenticatedUser(#userId) || @securityService.hasAdministratorAuthority()")
    public UserResponse editProfile(@PathVariable(name = "userId") String userId,@Valid @RequestBody UserEditProfileRequest userEditProfileRequest) {
        return userService.editProfile(userId, userEditProfileRequest);
    }

    @GetMapping("/me")
    public UserResponse getMyInformation() {
        return userService.getMyInformation();
    }
    @GetMapping("")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public Page<UserResponse> getAllUsers(UserSpecificationRequest userSpecificationRequest) {
        return userService.getAllUsers(userSpecificationRequest);
    }

    @GetMapping("/available-roles")
    public List<RoleResponse> getAllRoles(){
        return roleService.getAllRoles();
    }
}
