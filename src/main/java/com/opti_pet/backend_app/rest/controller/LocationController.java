package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.LocationAddUserRolesRequest;
import com.opti_pet.backend_app.rest.request.LocationCreateRequest;
import com.opti_pet.backend_app.rest.request.LocationCreateUserRequest;
import com.opti_pet.backend_app.rest.request.LocationUserRolesEditRequest;
import com.opti_pet.backend_app.rest.response.LocationResponse;
import com.opti_pet.backend_app.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public LocationResponse createLocation(@RequestBody LocationCreateRequest locationCreateRequest) {
        return locationService.createLocation(locationCreateRequest);
    }

    @PostMapping("/{locationId}/add-new-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #locationId) || @securityService.hasAdministratorAuthority()")
    public LocationResponse addNewEmployee(@PathVariable("locationId") String locationId, @RequestBody LocationCreateUserRequest locationCreateUserRequest) {
        return locationService.addNewEmployee(locationId, locationCreateUserRequest);
    }

    @PostMapping("/{locationId}/add-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #locationId) || @securityService.hasAdministratorAuthority()")
    public LocationResponse addNewEmployee(@PathVariable("locationId") String locationId, @RequestBody LocationAddUserRolesRequest locationAddUserRolesRequest) {
        return locationService.addRolesToExistingEmployee(locationId, locationAddUserRolesRequest);
    }

    @PutMapping("/{locationId}/remove-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #locationId) || @securityService.hasAdministratorAuthority()")
    public LocationResponse removeRolesFromEmployee(@PathVariable("locationId") String locationId, @RequestBody LocationUserRolesEditRequest locationUserRolesEditRequest) {
        return locationService.removeRolesFromEmployee(locationId, locationUserRolesEditRequest);
    }

    @PutMapping("/{locationId}/remove-employee/{employeeEmail}")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #locationId) || @securityService.hasAdministratorAuthority()")
    public LocationResponse removeEmployeeFromLocation(@PathVariable("locationId") String locationId, @PathVariable("employeeEmail") String employeeEmail) {
        return locationService.removeEmployeeFromLocation(locationId, employeeEmail);
    }

    @GetMapping("/{locationId}")
    public LocationResponse getLocationById(@PathVariable("locationId") String locationId) {
        return locationService.getLocationById(locationId);
    }
}
