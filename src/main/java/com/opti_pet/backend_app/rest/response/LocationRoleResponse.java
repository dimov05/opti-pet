package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.util.List;

@Builder
public record LocationRoleResponse(String locationId, String locationName, List<String> roles, boolean restrictByGps) {
}
