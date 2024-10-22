package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ClinicRepository extends JpaRepository<Clinic, UUID>, JpaSpecificationExecutor<Clinic> {
    boolean existsByName(String name);
}
