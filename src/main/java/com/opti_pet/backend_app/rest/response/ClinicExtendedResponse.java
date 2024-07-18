package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicExtendedResponse(String id, String name, String clinicEmail, String ownerEmail, String ownerPhoneNumber,
                                     String ownerName, Boolean isActive, List<LocationResponse> locations) {
}
