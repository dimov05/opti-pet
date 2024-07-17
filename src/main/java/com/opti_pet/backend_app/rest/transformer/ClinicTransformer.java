package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.response.ClinicResponse;

import java.util.ArrayList;

public class ClinicTransformer {
    public static Clinic toEntity(ClinicCreateRequest clinicCreateRequest, User owner) {
        return Clinic.builder()
                .name(clinicCreateRequest.name())
                .email(clinicCreateRequest.clinicEmail())
                .ownerName(owner.getName())
                .ownerPhoneNumber(owner.getPhoneNumber())
                .ownerEmail(owner.getEmail())
                .isActive(true)
                .locations(new ArrayList<>())
                .build();
    }

    public static ClinicResponse toResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId().toString())
                .name(clinic.getName())
                .clinicEmail(clinic.getEmail())
                .ownerName(clinic.getOwnerName())
                .ownerPhoneNumber(clinic.getOwnerPhoneNumber())
                .ownerEmail(clinic.getOwnerEmail())
                .isActive(clinic.isActive())
                .build();
    }
}
