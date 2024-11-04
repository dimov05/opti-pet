package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.rest.response.RoleResponse;

public class RoleTransformer {

    public static RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}