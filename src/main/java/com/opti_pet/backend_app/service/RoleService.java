package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.opti_pet.backend_app.util.AppConstants.ID_FIELD_NAME;
import static com.opti_pet.backend_app.util.AppConstants.ROLE_ENTITY;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public Role getRoleByIdOrThrowException(long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ROLE_ENTITY, ID_FIELD_NAME, String.valueOf(roleId)));
    }
}
