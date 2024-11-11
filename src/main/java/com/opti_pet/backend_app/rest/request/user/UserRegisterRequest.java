package com.opti_pet.backend_app.rest.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRegisterRequest(@NotBlank String email,@NotBlank String password,@NotBlank String confirmPassword,@NotBlank String name,
                                  @NotBlank String phoneNumber, String homeAddress,
                                  String bulstat,@NotBlank String jobTitle) {
}
