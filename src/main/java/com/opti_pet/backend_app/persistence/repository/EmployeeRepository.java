package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
