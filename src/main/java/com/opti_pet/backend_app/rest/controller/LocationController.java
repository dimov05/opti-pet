package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.LocationCreateRequest;
import com.opti_pet.backend_app.rest.response.LocationResponse;
import com.opti_pet.backend_app.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
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
}
