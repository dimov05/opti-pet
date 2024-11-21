package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleClinicRepository extends JpaRepository<UserRoleClinic, UUID>, JpaSpecificationExecutor<UserRoleClinic> {
    Optional<UserRoleClinic> findByUserIdAndRoleIdAndClinicId(UUID userId, Long roleId, UUID clinicId);

    void deleteByUserIdAndClinicId(UUID userId, UUID clinicId);

    List<UserRoleClinic> findAllByClinicId(UUID clinicId);
}
