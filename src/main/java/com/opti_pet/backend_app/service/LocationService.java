package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.LocationRepository;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.rest.request.LocationAddUserRolesRequest;
import com.opti_pet.backend_app.rest.request.LocationCreateRequest;
import com.opti_pet.backend_app.rest.request.LocationCreateUserRequest;
import com.opti_pet.backend_app.rest.request.LocationUserRolesEditRequest;
import com.opti_pet.backend_app.rest.response.LocationResponse;
import com.opti_pet.backend_app.rest.transformer.LocationTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.LOCATION_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserRoleLocationService userRoleLocationService;
    private final UserService userService;
    private final RoleRepository roleRepository;


    public Location getLocationByIdOrThrowException(UUID locationUuid) {
        return locationRepository.findById(locationUuid)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, locationUuid.toString()));
    }

    @Transactional
    public LocationResponse createLocation(LocationCreateRequest locationCreateRequest) {
        User owner = userService.getUserByEmailOrThrowException(locationCreateRequest.ownerEmail() != null ? locationCreateRequest.ownerEmail() : "admin@opti-pet.com");

        checkIfLocationAlreadyExists(locationCreateRequest);
        Location location = locationRepository.save(LocationTransformer.toEntity(locationCreateRequest));

        userRoleLocationService.createRoleForOwnerOfLocation(location, owner.getEmail());

        return LocationTransformer.toResponse(location);

    }

    private void checkIfLocationAlreadyExists(LocationCreateRequest locationCreateRequest) {
        if (locationRepository.existsByName(locationCreateRequest.name())) {
            throw new BadRequestException("Location with this name already exists");
        }
    }

    @Transactional
    public LocationResponse addNewEmployee(String locationId, LocationCreateUserRequest locationCreateUserRequest) {
        Location location = getLocationByIdOrThrowException(UUID.fromString(locationId));
        User user = userService.registerUserAsManager(locationCreateUserRequest);
        List<Role> roles = roleRepository.findAllById(locationCreateUserRequest.roleIdsToSet());
        roles.forEach(role -> userRoleLocationService.saveNewUserRoleLocation(user, location, role));

        return LocationTransformer.toResponse(location);
    }

    @Transactional
    public LocationResponse addRolesToExistingEmployee(String locationId, LocationAddUserRolesRequest locationAddUserRolesRequest) {
        Location location = getLocationByIdOrThrowException(UUID.fromString(locationId));
        User user = userService.getUserByEmailOrThrowException(locationAddUserRolesRequest.userEmail());
        List<Role> roles = roleRepository.findAllById(locationAddUserRolesRequest.roleIdsToSet());
        roles.forEach(role -> userRoleLocationService.saveNewUserRoleLocation(user, location, role));

        return LocationTransformer.toResponse(location);
    }

    @Transactional
    public LocationResponse removeRolesFromEmployee(String locationId, LocationUserRolesEditRequest locationUserRolesEditRequest) {
        Location location = getLocationByIdOrThrowException(UUID.fromString(locationId));
        User user = userService.getUserByEmailOrThrowException(locationUserRolesEditRequest.userEmail());
        List<Role> roles = roleRepository.findAllById(locationUserRolesEditRequest.roleIdsToSet());
        roles.forEach(role -> userRoleLocationService.deleteUserRoleLocation(user, location, role));

        return LocationTransformer.toResponse(location);
    }

    @Transactional
    public LocationResponse removeEmployeeFromLocation(String locationId, String employeeEmail) {
        Location location = getLocationByIdOrThrowException(UUID.fromString(locationId));
        User user = userService.getUserByEmailOrThrowException(employeeEmail);

        userRoleLocationService.deleteUserRoleLocationByUserAndLocation(user, location);

        return LocationTransformer.toResponse(location);
    }

    public LocationResponse getLocationById(String locationId) {
        return LocationTransformer.toResponse(getLocationByIdOrThrowException(UUID.fromString(locationId)));
    }
}
