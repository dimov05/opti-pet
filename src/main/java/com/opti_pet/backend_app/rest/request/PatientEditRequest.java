package com.opti_pet.backend_app.rest.request;

import com.opti_pet.backend_app.persistence.enums.PetType;
import lombok.Builder;

@Builder
public record PatientEditRequest(String ownerEmail, String name, PetType petType,
                                 String birthdate, String microchip, String pendant, String passport,
                                 Double weight, Boolean isDeceased, Boolean isNeutered, String patientAccessCode,
                                 String note) {
}