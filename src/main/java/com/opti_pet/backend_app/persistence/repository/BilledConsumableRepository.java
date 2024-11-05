package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.BilledConsumable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BilledConsumableRepository extends JpaRepository<BilledConsumable, UUID> {
}
