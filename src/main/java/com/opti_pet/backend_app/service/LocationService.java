package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.LOCATION_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;


    public Location getLocationByIdOrThrowException(UUID locationUuid) {
        return locationRepository.findById(locationUuid)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, locationUuid.toString()));
    }
}
