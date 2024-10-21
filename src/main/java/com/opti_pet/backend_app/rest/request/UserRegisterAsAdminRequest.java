package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRegisterAsAdminRequest(@NotBlank String email, @NotBlank String name, @NotBlank String phoneNumber,
                                         String homeAddress, @NotBlank String jobTitle) {
}
