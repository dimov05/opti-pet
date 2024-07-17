package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ClinicCreateRequest(@Email String ownerEmail, @NotBlank String name, @Email String clinicEmail) {
}
