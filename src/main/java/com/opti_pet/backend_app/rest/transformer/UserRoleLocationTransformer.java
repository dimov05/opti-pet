package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleLocation;
import com.opti_pet.backend_app.rest.response.LocationRoleResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static List<LocationRoleResponse> toLocationRoleResponse(List<UserRoleLocation> userRoleLocations) {
        HashMap<UUID, List<String>> rolesByLocation = new HashMap<>();
        userRoleLocations.forEach(userRoleLocation -> {
            if (rolesByLocation.containsKey(userRoleLocation.getLocation().getId())) {
                List<String> currentRoles = rolesByLocation.get(userRoleLocation.getLocation().getId());
                currentRoles.add(userRoleLocation.getRole().getName());
                rolesByLocation.put(userRoleLocation.getLocation().getId(),currentRoles);
            } else{
                List<String> currentRoles = new ArrayList<>();
                currentRoles.add(userRoleLocation.getRole().getName());
                rolesByLocation.put(userRoleLocation.getLocation().getId(),currentRoles);
            }
        });
        return userRoleLocations.stream()
                .map(userRoleLocation -> LocationRoleResponse.builder()
                        .locationId(userRoleLocation.getLocation().getId().toString())
                        .locationName(userRoleLocation.getLocation().getName())
                        .roles(rolesByLocation.get(userRoleLocation.getLocation().getId()))
                        .restrictByGps(userRoleLocation.getLocation().getLocationRestrictionsEnabled())
                        .build())
                .collect(Collectors.toSet())
                .stream().toList();
    }
}
