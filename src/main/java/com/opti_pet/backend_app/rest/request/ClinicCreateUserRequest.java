package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record ClinicCreateUserRequest(@NotBlank String email, String userPhoneNumber, String userName,
                                      String userJobTitle, String homeAddress, String bulstat, List<Long> roleIdsToSet) {
}
