package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import com.opti_pet.backend_app.persistence.repository.ClinicRepository;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.persistence.repository.UserRoleClinicRepository;
import com.opti_pet.backend_app.rest.request.clinic.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.request.clinic.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.clinic.ClinicUpdateRequest;
import com.opti_pet.backend_app.rest.request.clinic.ClinicUserRolesEditRequest;
import com.opti_pet.backend_app.rest.request.specification.BaseSpecificationRequest;
import com.opti_pet.backend_app.rest.response.ClinicBaseResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.rest.transformer.ClinicTransformer;
import com.opti_pet.backend_app.rest.transformer.UserTransformer;
import com.opti_pet.backend_app.util.specifications.ClinicSpecifications;
import com.opti_pet.backend_app.util.specifications.UserRoleClinicSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.opti_pet.backend_app.util.AppConstants.*;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRoleClinicService userRoleClinicService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRoleClinicRepository userRoleClinicRepository;


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

    @Transactional
    public ClinicResponse addNewEmployee(String clinicId, ClinicCreateUserRequest clinicCreateUserRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.registerUserAsManager(clinicCreateUserRequest);
        List<Role> roles = roleRepository.findAllById(clinicCreateUserRequest.roleIdsToSet());
        roles.forEach(role -> userRoleClinicService.saveNewUserRoleClinic(user, clinic, role));

        return ClinicTransformer.toResponse(clinic);
    }

    @Transactional
    public ClinicResponse removeEmployeeFromClinic(String clinicId, String employeeEmail) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.getUserByEmailOrThrowException(employeeEmail);

        userRoleClinicService.deleteUserRoleClinicByUserAndClinic(user, clinic);

        return ClinicTransformer.toResponse(clinic);
    }

    @Transactional
    public UserResponse setRolesToEmployeeForClinic(String clinicId, ClinicUserRolesEditRequest clinicUserRolesEditRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User user = userService.getUserByEmailOrThrowException(clinicUserRolesEditRequest.userEmail());
        List<Role> roles = roleRepository.findAllById(clinicUserRolesEditRequest.roleIdsToSet());

        List<UserRoleClinic> useRoleClinicsToAdd = userRoleClinicService.setRolesForUserAndClinic(user, roles, clinic);
        List<UserRoleClinic> userRoleClinics = user.getUserRoleClinics();
        userRoleClinics.addAll(useRoleClinicsToAdd);
        user.setUserRoleClinics(userRoleClinics);

        return UserTransformer.toResponse(user);
    }

    @Transactional
    public List<UserResponse> getAllEmployees(String clinicId) {
        List<UserRoleClinic> userRoleClinics = userRoleClinicRepository.findAllByClinicId(UUID.fromString(clinicId));

        return userRoleClinics.stream()
                .map(UserRoleClinic::getUser)
                .map(UserTransformer::toResponse)
                .collect(Collectors.toSet())
                .stream().toList();
    }

    @Transactional
    public List<ClinicBaseResponse> getAllClinicsBaseResponse() {
        return clinicRepository.findAll()
                .stream().filter(clinic -> !clinic.getId().equals(DEFAULT_CLINIC_UUID))
                .map(ClinicTransformer::toBaseResponse)
                .toList();
    }

    @Transactional
    public Page<ClinicResponse> getAllClinicsExtendedResponse(BaseSpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);

        return clinicRepository.findAll(getSpecifications(specificationRequest), pageRequest)
                .map(ClinicTransformer::toResponse);
    }

    @Transactional
    public ClinicResponse updateClinic(String clinicId, ClinicUpdateRequest clinicUpdateRequest) {
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User owner = userService.getUserByEmailOrThrowException(clinicUpdateRequest.ownerEmail() != null ? clinicUpdateRequest.ownerEmail() : "admin@opti-pet.com");

        if (clinic.getOwner().getId() != owner.getId()) {
            clinic.setOwner(owner);
        }
        updateClinicField(clinicUpdateRequest::name, clinic::getName, clinic::setName);
        updateClinicField(clinicUpdateRequest::email, clinic::getEmail, clinic::setEmail);
        updateClinicField(clinicUpdateRequest::city, clinic::getCity, clinic::setCity);
        updateClinicField(clinicUpdateRequest::address, clinic::getAddress, clinic::setAddress);
        updateClinicField(clinicUpdateRequest::phoneNumber, clinic::getPhoneNumber, clinic::setPhoneNumber);
        if (!clinicUpdateRequest.clinicRestrictionsEnabled().equals(clinic.getClinicRestrictionsEnabled())) {
            if (Boolean.TRUE.equals(clinicUpdateRequest.clinicRestrictionsEnabled())) {
                clinic.setClinicRestrictionsEnabled(true);
                if (clinicUpdateRequest.longitude() == null || clinicUpdateRequest.latitude() == null) {
                    throw new BadRequestException("You should send valid Latitude/Longitude values!");
                }
                clinic.setLongitude(clinicUpdateRequest.longitude());
                clinic.setLatitude(clinicUpdateRequest.latitude());
            }
            if (Boolean.FALSE.equals(clinicUpdateRequest.clinicRestrictionsEnabled())) {
                clinic.setClinicRestrictionsEnabled(false);
                clinic.setLatitude(null);
                clinic.setLongitude(null);
            }
        }

        return ClinicTransformer.toResponse(clinicRepository.save(clinic));
    }

    @Transactional
    public Page<UserResponse> getAllEmployeesByClinicIdForManager(String clinicId, BaseSpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);
        Clinic clinic = getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return userRoleClinicRepository.findAll(getSpecifications(specificationRequest, clinic), pageRequest)
                .map(UserRoleClinic::getUser)
                .map(UserTransformer::toResponse);
    }

    private void updateClinicField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private void checkIfClinicAlreadyExists(ClinicCreateRequest clinicCreateRequest) {
        if (clinicRepository.existsByName(clinicCreateRequest.name())) {
            throw new BadRequestException("Clinic with this name already exists");
        }
    }

    public ClinicResponse getClinicById(String clinicId) {
        return ClinicTransformer.toResponse(getClinicByIdOrThrowException(UUID.fromString(clinicId)));
    }

    private Specification<Clinic> getSpecifications(BaseSpecificationRequest specificationRequest) {
        String inputText = specificationRequest.inputText();
        Specification<Clinic> specification = Specification.where(ClinicSpecifications.clinicIsNotDefaultClinic());
        if (inputText != null) {
            specification = specification.and(ClinicSpecifications.clinicEmailOrNameOrCityOrAddressOrPhoneNumberLike(inputText));
        }

        return specification;
    }

    private Specification<UserRoleClinic> getSpecifications(BaseSpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<UserRoleClinic> specification = UserRoleClinicSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(UserRoleClinicSpecifications.userRoleClinicEmployeeNameOrEmailLike(inputText));
        }

        return specification;
    }

    private Pageable createPageRequest(BaseSpecificationRequest specificationRequest) {
        int pageNumber = specificationRequest.pageNumber() != null ? specificationRequest.pageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = specificationRequest.pageSize() != null ? specificationRequest.pageSize() : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize);
    }
}
