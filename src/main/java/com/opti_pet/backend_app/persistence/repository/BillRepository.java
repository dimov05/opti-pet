package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID>, JpaSpecificationExecutor<Bill> {
    List<Bill> findTop500ByClinic_IdOrderByOpenDateDesc(UUID clinicId);

    List<Bill> findAllByClinic_Id(UUID clinicId);
}
