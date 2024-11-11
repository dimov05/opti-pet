package com.opti_pet.backend_app.rest.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserEditProfileRequest(@NotBlank String name, @NotBlank String phoneNumber,String homeAddress, String bulstat,
                                     @NotBlank String jobTitle, String note) {
}
