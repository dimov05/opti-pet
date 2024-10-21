package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import com.opti_pet.backend_app.rest.response.ClinicRoleResponse;
import com.opti_pet.backend_app.rest.response.RoleResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserRoleClinicTransformer {
    public static UserRoleClinic fromData(User user, Clinic clinic, Role role) {
        return UserRoleClinic.builder()
                .userId(user.getId())
                .user(user)
                .clinic(clinic)
                .clinicId(clinic.getId())
                .role(role)
                .roleId(role.getId())
                .build();
    }

    public static List<ClinicRoleResponse> toClinicRoleResponse(List<UserRoleClinic> userRoleClinics) {
        HashMap<UUID, List<RoleResponse>> rolesByClinic = new HashMap<>();
        userRoleClinics.forEach(userRoleClinic -> {
            if (rolesByClinic.containsKey(userRoleClinic.getClinic().getId())) {
                List<RoleResponse> currentRoles = rolesByClinic.get(userRoleClinic.getClinic().getId());
                currentRoles.add(RoleTransformer.toResponse(userRoleClinic.getRole()));
                rolesByClinic.put(userRoleClinic.getClinic().getId(), currentRoles);
            } else {
                List<RoleResponse> currentRoles = new ArrayList<>();
                currentRoles.add(RoleTransformer.toResponse(userRoleClinic.getRole()));
                rolesByClinic.put(userRoleClinic.getClinic().getId(), currentRoles);
            }
        });
        return userRoleClinics.stream()
                .map(userRoleClinic -> ClinicRoleResponse.builder()
                        .clinicId(userRoleClinic.getClinic().getId().toString())
                        .clinicName(userRoleClinic.getClinic().getName())
                        .roles(rolesByClinic.get(userRoleClinic.getClinic().getId()))
                        .restrictByGps(userRoleClinic.getClinic().getClinicRestrictionsEnabled())
                        .build())
                .collect(Collectors.toSet())
                .stream().toList();
    }
}
