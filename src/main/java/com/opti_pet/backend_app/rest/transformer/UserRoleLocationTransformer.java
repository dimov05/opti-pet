package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleLocation;

public class UserRoleLocationTransformer {
    public static UserRoleLocation fromData(User user, Location location, Role role) {
        return UserRoleLocation.builder()
                .userId(user.getId())
                .user(user)
                .location(location)
                .locationId(location.getId())
                .role(role)
                .roleId(role.getId())
                .build();
    }
}
