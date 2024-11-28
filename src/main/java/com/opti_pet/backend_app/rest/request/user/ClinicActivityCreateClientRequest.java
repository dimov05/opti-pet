package com.opti_pet.backend_app.rest.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ClinicActivityCreateClientRequest(String email, @NotBlank String name, @NotBlank String phoneNumber,
                                                String homeAddress, String bulstat, String note) {
}
