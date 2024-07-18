package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.rest.request.LocationCreateRequest;
import com.opti_pet.backend_app.rest.response.LocationResponse;

import java.util.ArrayList;

public class LocationTransformer {
    public static Location toEntity(LocationCreateRequest locationCreateRequest, Clinic clinic) {
        return Location.builder()
                .name(locationCreateRequest.name())
                .email(locationCreateRequest.email())
                .city(locationCreateRequest.city())
                .address(locationCreateRequest.address())
                .phoneNumber(locationCreateRequest.phoneNumber())
                .locationRestrictionsEnabled(locationCreateRequest.locationRestrictionsEnabled())
                .latitude(locationCreateRequest.latitude())
                .longitude(locationCreateRequest.longitude())
                .isActive(true)
                .clinic(clinic)
                .vaccinations(new ArrayList<>())
                .discounts(new ArrayList<>())
                .notes(new ArrayList<>())
                .bills(new ArrayList<>())
                .items(new ArrayList<>())
                .procedures(new ArrayList<>())
                .billedItems(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .patients(new ArrayList<>())
                .userRoleLocations(new ArrayList<>())
                .build();
    }

    public static LocationResponse toResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId().toString())
                .clinicId(location.getClinic().getId().toString())
                .name(location.getName())
                .email(location.getEmail())
                .city(location.getCity())
                .address(location.getAddress())
                .phoneNumber(location.getPhoneNumber())
                .locationRestrictionsEnabled(location.getLocationRestrictionsEnabled())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isActive(location.isActive())
                .build();
    }
}
