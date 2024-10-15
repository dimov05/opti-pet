package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleClinicRepository extends JpaRepository<UserRoleClinic, UUID> {
    Optional<UserRoleClinic> findByUserIdAndRoleIdAndClinicId(UUID userId, Long roleId, UUID clinicId);

    void deleteByUserIdAndClinicId(UUID userId, UUID clinicId);
}
