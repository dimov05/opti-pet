package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface MedicationRepository extends JpaRepository<Medication, UUID>, JpaSpecificationExecutor<Medication> {
    List<Medication> findAllByClinic_Id(UUID clinicId);
    List<Medication> findAllByIdIn(List<UUID> ids);
}
