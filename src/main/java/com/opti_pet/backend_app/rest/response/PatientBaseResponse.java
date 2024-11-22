package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record PatientBaseResponse(String id, String name, String petType, String birthdate) {
}