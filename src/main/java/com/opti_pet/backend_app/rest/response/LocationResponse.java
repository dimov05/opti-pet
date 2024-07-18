package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record LocationResponse(String id, String clinicId, String name, String email, String city, String address,
                               String phoneNumber, Boolean locationRestrictionsEnabled, Double latitude,
                               Double longitude, Boolean isActive) {
}
