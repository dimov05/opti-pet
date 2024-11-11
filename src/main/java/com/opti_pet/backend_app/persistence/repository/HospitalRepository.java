package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface HospitalRepository extends JpaRepository<Hospital, UUID>, JpaSpecificationExecutor<Hospital> {
    List<Hospital> findAllByClinic_Id(UUID clinicId);
}