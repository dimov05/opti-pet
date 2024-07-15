package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
}
