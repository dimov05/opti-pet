package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record RoleResponse(String id, String name, String description) {
}
