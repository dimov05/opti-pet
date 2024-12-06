package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.hospital.HospitalCreateRequest;
import com.opti_pet.backend_app.rest.request.hospital.HospitalUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.SpecificationRequest;
import com.opti_pet.backend_app.rest.response.HospitalBaseResponse;
import com.opti_pet.backend_app.rest.response.HospitalResponse;
import com.opti_pet.backend_app.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class HospitalController {
    private final HospitalService hospitalService;

    @GetMapping("/clinics/{clinicId}/hospitals")
    public List<HospitalResponse> getAllHospitalsByClinicId(@PathVariable(name = "clinicId") String clinicId) {
        return hospitalService.getAllHospitalsByClinicId(clinicId);
    }

    @GetMapping("/clinics/{clinicId}/manager/hospitals")
    public Page<HospitalResponse> getAllHospitalsByClinicIdForManager(@PathVariable(name = "clinicId") String clinicId, SpecificationRequest specificationRequest) {
        return hospitalService.getAllHospitalsByClinicIdForManager(clinicId, specificationRequest);
    }

    @PostMapping("/clinics/{clinicId}/hospitals")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public HospitalResponse createHospital(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody HospitalCreateRequest hospitalCreateRequest) {
        return hospitalService.createHospital(clinicId, hospitalCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/hospitals/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public HospitalResponse updateHospital(@PathVariable("clinicId") String clinicId, @Valid @RequestBody HospitalUpdateRequest hospitalUpdateRequest) {
        return hospitalService.updateHospital(hospitalUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/hospitals/{hospitalId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public HospitalResponse deleteHospitalById(@PathVariable("clinicId") String clinicId, @PathVariable("hospitalId") String hospitalId) {
        return hospitalService.deleteHospitalById(hospitalId);
    }
}
