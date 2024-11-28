package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.patient.PatientCreateRequest;
import com.opti_pet.backend_app.rest.request.user.ClinicActivityCreateClientRequest;
import com.opti_pet.backend_app.rest.response.ClientExtendedResponse;
import com.opti_pet.backend_app.rest.response.PatientResponse;
import com.opti_pet.backend_app.service.PatientService;
import com.opti_pet.backend_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clinical-activity")
@RequiredArgsConstructor
@Validated
public class ClinicalActivityController {
    private final UserService userService;
    private final PatientService patientService;

    @PostMapping("/{clinicId}/add-new-client")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public ClientExtendedResponse addNewClient(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicActivityCreateClientRequest clinicActivityCreateClientRequest) {
        return userService.addNewClient(clinicActivityCreateClientRequest);
    }

    @PostMapping("/{clinicId}/add-new-patient")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public PatientResponse addNewPatient(@PathVariable("clinicId") String clinicId, @Valid @RequestBody PatientCreateRequest patientCreateRequest) {
        return patientService.addNewPatient(patientCreateRequest);
    }

    @GetMapping("/{clinicId}/find-client/{phoneNumber}")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public ClientExtendedResponse findClient(@PathVariable("clinicId") String clinicId, @PathVariable("phoneNumber") String phoneNumber) {
        return userService.findClient(phoneNumber);
    }

}
