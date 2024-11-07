package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProcedureRepository extends JpaRepository<Procedure, UUID>, JpaSpecificationExecutor<Procedure> {
    List<Procedure> findAllByClinic_Id(UUID clinicId);
    List<Procedure> findAllByIdIn(List<UUID> ids);
}
