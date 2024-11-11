package com.opti_pet.backend_app.rest.request.clinic;

import lombok.Builder;

import java.util.List;

@Builder
public record ClinicUserRolesEditRequest(String userEmail, List<Long> roleIdsToSet) {
}
