package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResponse(String id, String name, String email, String phoneNumber, String homeAddress, String bulstat,
                           String jobTitle, String note, Boolean isActive, Boolean isAdministrator,
                           List<ClinicRoleResponse> clinics) {
}
