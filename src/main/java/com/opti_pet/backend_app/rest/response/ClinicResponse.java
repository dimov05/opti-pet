package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record ClinicResponse(String id, String name, String email, String city, String address,
                               String phoneNumber, Boolean clinicRestrictionsEnabled, Double latitude,
                               Double longitude, int employeesCount, Boolean isActive) {
}
