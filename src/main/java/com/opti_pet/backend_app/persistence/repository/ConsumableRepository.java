package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Consumable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumableRepository extends JpaRepository<Consumable, UUID> {
}
