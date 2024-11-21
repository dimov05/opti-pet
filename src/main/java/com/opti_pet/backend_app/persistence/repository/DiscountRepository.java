package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, Long>, JpaSpecificationExecutor<Discount> {
    List<Discount> findAllByClinic_Id(UUID clinicId);
}
