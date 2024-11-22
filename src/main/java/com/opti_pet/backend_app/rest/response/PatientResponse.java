package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record PatientResponse(String id, String name, String petType, String birthdate, String microchip,
                              String pendant, String passport, double weight, boolean isDeceased, boolean isNeutered,
                              String patientAccessCode, String note, UserResponse owner) {
}