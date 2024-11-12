package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BillTemplateRepository extends JpaRepository<BillTemplate, UUID> {
    List<BillTemplate> findBillTemplateByClinic_Id(UUID clinicId);
}
