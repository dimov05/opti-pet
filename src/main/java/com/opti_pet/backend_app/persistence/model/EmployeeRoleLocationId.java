package com.opti_pet.backend_app.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRoleLocationId implements Serializable {
    private UUID employeeId;
    private Long roleId;
    private UUID locationId;
}
