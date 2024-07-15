package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
