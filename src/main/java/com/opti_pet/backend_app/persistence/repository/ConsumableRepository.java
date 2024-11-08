package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Consumable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ConsumableRepository extends JpaRepository<Consumable, UUID>, JpaSpecificationExecutor<Consumable> {
    List<Consumable> findAllByClinic_Id(UUID clinicId);

    List<Consumable> findAllByIdIn(List<UUID> ids);
}
