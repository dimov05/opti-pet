package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface BillTemplateRepository extends JpaRepository<BillTemplate, UUID>, JpaSpecificationExecutor<BillTemplate> {
    List<BillTemplate> findBillTemplateByClinic_Id(UUID clinicId);
}
