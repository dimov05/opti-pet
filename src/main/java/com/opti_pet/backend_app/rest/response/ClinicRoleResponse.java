package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicRoleResponse(String clinicId, String clinicName, List<String> roles, boolean restrictByGps) {
}
