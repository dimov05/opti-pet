package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {
}
