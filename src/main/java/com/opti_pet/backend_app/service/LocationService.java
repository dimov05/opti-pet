package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.LocationRepository;
import com.opti_pet.backend_app.rest.request.LocationCreateRequest;
import com.opti_pet.backend_app.rest.response.LocationResponse;
import com.opti_pet.backend_app.rest.transformer.LocationTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.LOCATION_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final ClinicService clinicService;
    private final UserRoleLocationService userRoleLocationService;


    public Location getLocationByIdOrThrowException(UUID locationUuid) {
        return locationRepository.findById(locationUuid)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, locationUuid.toString()));
    }
@Transactional
    public LocationResponse createLocation(LocationCreateRequest locationCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(locationCreateRequest.clinicId());

        checkIfLocationAlreadyExists(locationCreateRequest);
        Location location = locationRepository.save(LocationTransformer.toEntity(locationCreateRequest, clinic));

        userRoleLocationService.createRoleForOwnerOfLocation(location,clinic.getOwnerEmail());

        return LocationTransformer.toResponse(location);

    }

    private void checkIfLocationAlreadyExists(LocationCreateRequest locationCreateRequest) {
        if (locationRepository.existsByName(locationCreateRequest.name())) {
            throw new BadRequestException("Location with this name already exists");
        }
    }
}
