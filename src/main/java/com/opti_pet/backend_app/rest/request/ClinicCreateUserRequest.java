package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicCreateUserRequest(String userEmail, String userPassword, String userConfirmPassword,
                                        String userPhoneNumber, String userName, String userJobTitle,
                                        List<Long> roleIdsToSet) {
}
