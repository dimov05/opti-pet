package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAllByClinic_Id(UUID clinicId);
}
