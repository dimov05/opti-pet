package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.response.ClinicExtendedResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinic")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public ClinicResponse createClinic(@RequestBody ClinicCreateRequest clinicCreateRequest) {
        return clinicService.createClinic(clinicCreateRequest);
    }

    @GetMapping("/{clinicId}")
    public ClinicExtendedResponse getClinic(@PathVariable UUID clinicId) {
        return clinicService.getClinicByIdOrThrowException(clinicId);
    }
}
