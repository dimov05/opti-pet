package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.rest.response.RoleResponse;
import com.opti_pet.backend_app.rest.transformer.RoleTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.opti_pet.backend_app.util.AppConstants.ID_FIELD_NAME;
import static com.opti_pet.backend_app.util.AppConstants.ROLE_ENTITY;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role getRoleByIdOrThrowException(long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ROLE_ENTITY, ID_FIELD_NAME, String.valueOf(roleId)));
    }

    @Transactional
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .filter(role -> !role.getId().equals(6L) && !role.getId().equals(1L))
                .map(RoleTransformer::toResponse)
                .toList();
    }
}
