package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.UserRoleLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleLocationRepository extends JpaRepository<UserRoleLocation, UUID> {
    Optional<UserRoleLocation> findByUserIdAndRoleIdAndLocationId(UUID userId, Long roleId, UUID locationId);
}
