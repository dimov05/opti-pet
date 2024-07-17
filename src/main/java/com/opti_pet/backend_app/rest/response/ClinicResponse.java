package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record ClinicResponse(String id, String name, String clinicEmail, String ownerEmail, String ownerPhoneNumber,
                             String ownerName, Boolean isActive) {
}
