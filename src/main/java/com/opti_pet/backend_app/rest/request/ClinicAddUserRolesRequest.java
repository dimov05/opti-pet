package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicAddUserRolesRequest(String userEmail, List<Long> roleIdsToSet) {
}
