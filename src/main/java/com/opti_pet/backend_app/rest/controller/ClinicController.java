package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ClinicAddUserRolesRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.ClinicUserRolesEditRequest;
import com.opti_pet.backend_app.rest.response.ClinicBaseResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinic")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public ClinicResponse createClinic(@Valid @RequestBody ClinicCreateRequest clinicCreateRequest) {
        return clinicService.createClinic(clinicCreateRequest);
    }

    @PostMapping("/{clinicId}/add-new-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse addNewEmployee(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicCreateUserRequest clinicCreateUserRequest) {
        return clinicService.addNewEmployee(clinicId, clinicCreateUserRequest);
    }

    @PostMapping("/{clinicId}/add-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse addNewEmployee(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicAddUserRolesRequest clinicAddUserRolesRequest) {
        return clinicService.addRolesToExistingEmployee(clinicId, clinicAddUserRolesRequest);
    }

    @PutMapping("/{clinicId}/remove-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse removeRolesFromEmployee(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        return clinicService.removeRolesFromEmployee(clinicId, clinicUserRolesEditRequest);
    }

    @PutMapping("/{clinicId}/set-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public UserResponse setRolesToEmployeeForClinic(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        return clinicService.setRolesToEmployeeForClinic(clinicId, clinicUserRolesEditRequest);
    }

    @PutMapping("/{clinicId}/remove-employee/{employeeEmail}")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse removeEmployeeFromClinic(@PathVariable("clinicId") String clinicId, @PathVariable("employeeEmail") String employeeEmail) {
        return clinicService.removeEmployeeFromClinic(clinicId, employeeEmail);
    }

    @GetMapping("/{clinicId}")
    public ClinicResponse getClinicById(@PathVariable("clinicId") String clinicId) {
        return clinicService.getClinicById(clinicId);
    }

    @GetMapping("")
    public List<ClinicBaseResponse> getAllClinics() {
        return clinicService.getAllClinics();
    }
}
