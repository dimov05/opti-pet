package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.clinic.*;
import com.opti_pet.backend_app.rest.response.ClinicBaseResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public ClinicResponse createClinic(@Valid @RequestBody ClinicCreateRequest clinicCreateRequest) {
        return clinicService.createClinic(clinicCreateRequest);
    }

    @PutMapping("/{clinicId}/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse updateClinic(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicUpdateRequest clinicUpdateRequest) {
        return clinicService.updateClinic(clinicId, clinicUpdateRequest);
    }

    @PostMapping("/{clinicId}/add-new-employee")
    @PreAuthorize("@securityService.hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse addNewEmployee(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicCreateUserRequest clinicCreateUserRequest) {
        return clinicService.addNewEmployee(clinicId, clinicCreateUserRequest);
    }

    @PutMapping("/{clinicId}/set-roles-employee")
    @PreAuthorize("@securityService.hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public UserResponse setRolesToEmployeeForClinic(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        return clinicService.setRolesToEmployeeForClinic(clinicId, clinicUserRolesEditRequest);
    }

    @PutMapping("/{clinicId}/remove-employee/{employeeEmail}")
    @PreAuthorize("@securityService.hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse removeEmployeeFromClinic(@PathVariable("clinicId") String clinicId, @PathVariable("employeeEmail") String employeeEmail) {
        return clinicService.removeEmployeeFromClinic(clinicId, employeeEmail);
    }

    @GetMapping("/{clinicId}")
    public ClinicResponse getClinicById(@PathVariable("clinicId") String clinicId) {
        return clinicService.getClinicById(clinicId);
    }

    @GetMapping("")
    public List<ClinicBaseResponse> getAllClinicsBaseResponse() {
        return clinicService.getAllClinicsBaseResponse();
    }

    @GetMapping("/data")
    public Page<ClinicResponse> getAllClinicsExtendedResponse(ClinicSpecificationRequest clinicSpecificationRequest) {
        return clinicService.getAllClinicsExtendedResponse(clinicSpecificationRequest);
    }

    @GetMapping("/{clinicId}/employees")
    public List<UserResponse> getAllEmployees(@PathVariable("clinicId") String clinicId) {
        return clinicService.getAllEmployees(clinicId);
    }
}
