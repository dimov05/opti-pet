package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    boolean existsByName(String name);
}
