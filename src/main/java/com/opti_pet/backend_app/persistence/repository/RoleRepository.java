package com.opti_pet.backend_app.persistence.repository;

import com.opti_pet.backend_app.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
