package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcedureRepository extends JpaRepository<Procedure, UUID> {
}
