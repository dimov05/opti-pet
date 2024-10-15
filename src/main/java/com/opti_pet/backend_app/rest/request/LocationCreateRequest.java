package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record LocationCreateRequest(@NotBlank String name,@NotBlank String ownerEmail, @Email String email,
                                    @NotBlank String city, @NotBlank String address,
                                    @NotBlank String phoneNumber, @NotNull Boolean locationRestrictionsEnabled,
                                    Double latitude,
                                    Double longitude) {
}
