package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.ClinicRepository;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.rest.request.ClinicAddUserRolesRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.request.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.ClinicUserRolesEditRequest;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.rest.transformer.ClinicTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.CLINIC_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRoleClinicService userRoleClinicService;
    private final UserService userService;
    private final RoleRepository roleRepository;


    public Clinic getClinicByIdOrThrowException(UUID clinicUuid) {
        return clinicRepository.findById(clinicUuid)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, clinicUuid.toString()));
    }

    @Transactional
    public ClinicResponse createClinic(ClinicCreateRequest clinicCreateRequest) {
        User owner = userService.getUserByEmailOrThrowException(clinicCreateRequest.ownerEmail() != null ? clinicCreateRequest.ownerEmail() : "admin@opti-pet.com");

        checkIfClinicAlreadyExists(clinicCreateRequest);
        Clinic clinic = clinicRepository.save(ClinicTransformer.toEntity(clinicCreateRequest, owner));

        userRoleClinicService.createRoleForOwnerOfClinic(clinic, owner.getEmail());

        return ClinicTransformer.toResponse(clinic);

    }

    private void checkIfClinicAlreadyExists(ClinicCreateRequest clinicCreateRequest) {
        if (clinicRepository.existsByName(clinicCreateRequest.name())) {
            throw new BadRequestException("Clinic with this name already exists");
        }
    }

    @Transactional
    public ClinicResponse addNewEmployee(String clinicId, ClinicCreateUserRequest clinicCreateUserRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.registerUserAsManager(clinicCreateUserRequest);
        List<Role> roles = roleRepository.findAllById(clinicCreateUserRequest.roleIdsToSet());
        roles.forEach(role -> userRoleClinicService.saveNewUserRoleClinic(user, clinic, role));

        return ClinicTransformer.toResponse(clinic);
    }

    @Transactional
    public ClinicResponse addRolesToExistingEmployee(String clinicId, ClinicAddUserRolesRequest clinicAddUserRolesRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.getUserByEmailOrThrowException(clinicAddUserRolesRequest.userEmail());
        List<Role> roles = roleRepository.findAllById(clinicAddUserRolesRequest.roleIdsToSet());
        roles.forEach(role -> userRoleClinicService.saveNewUserRoleClinic(user, clinic, role));

        return ClinicTransformer.toResponse(clinic);
    }

    @Transactional
    public ClinicResponse removeRolesFromEmployee(String clinicId, ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.getUserByEmailOrThrowException(clinicUserRolesEditRequest.userEmail());
        List<Role> roles = roleRepository.findAllById(clinicUserRolesEditRequest.roleIdsToSet());
        roles.forEach(role -> userRoleClinicService.deleteUserRoleClinic(user, clinic, role));

        return ClinicTransformer.toResponse(clinic);
    }

    @Transactional
    public ClinicResponse removeEmployeeFromClinic(String clinicId, String employeeEmail) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.getUserByEmailOrThrowException(employeeEmail);

        userRoleClinicService.deleteUserRoleClinicByUserAndClinic(user, clinic);

        return ClinicTransformer.toResponse(clinic);
    }

    public ClinicResponse getClinicById(String clinicId) {
        return ClinicTransformer.toResponse(getClinicByIdOrThrowException(UUID.fromString(clinicId)));
    }
}
