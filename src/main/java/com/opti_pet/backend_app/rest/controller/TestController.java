package com.opti_pet.backend_app.rest.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/user/test/{locationId}")
    @PreAuthorize("hasAuthority('OWNER_' + #locationId)")
    public String userProfile(@PathVariable(name = "locationId") String locationId) {
        return "Welcome to User Profile";
    }
    @GetMapping("/clinic-manager/test/{locationId}")
    @PreAuthorize("hasAuthority('CLINIC_MANAGER_' + #locationId) || @securityService.hasAdministratorAuthority()")
    public String managerProfile(@PathVariable(name = "locationId") String locationId) {
        return "Welcome to Clinic Manager Profile";
    }
}
