package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ClinicAddUserRolesRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.ClinicUserRolesEditRequest;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.service.ClinicService;
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
@RequestMapping("/api/v1/clinic")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAdministratorAuthority()")
    public ClinicResponse createClinic(@RequestBody ClinicCreateRequest clinicCreateRequest) {
        return clinicService.createClinic(clinicCreateRequest);
    }

    @PostMapping("/{clinicId}/add-new-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse addNewEmployee(@PathVariable("clinicId") String clinicId, @RequestBody ClinicCreateUserRequest clinicCreateUserRequest) {
        return clinicService.addNewEmployee(clinicId, clinicCreateUserRequest);
    }

    @PostMapping("/{clinicId}/add-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse addNewEmployee(@PathVariable("clinicId") String clinicId, @RequestBody ClinicAddUserRolesRequest clinicAddUserRolesRequest) {
        return clinicService.addRolesToExistingEmployee(clinicId, clinicAddUserRolesRequest);
    }

    @PutMapping("/{clinicId}/remove-roles-employee")
    @PreAuthorize("hasAuthority('PEOPLE_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ClinicResponse removeRolesFromEmployee(@PathVariable("clinicId") String clinicId, @RequestBody ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        return clinicService.removeRolesFromEmployee(clinicId, clinicUserRolesEditRequest);
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
}
