package com.opti_pet.backend_app.rest.request.patient;

import com.opti_pet.backend_app.persistence.enums.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PatientCreateRequest(@NotBlank String ownerPhoneNumber, @NotBlank String name, @NotNull PetType petType,
                                   @NotNull String birthdate, String microchip, String pendant, String passport,
                                   Double weight, Boolean isDeceased, Boolean isNeutered, String patientAccessCode,
                                   String note) {
}