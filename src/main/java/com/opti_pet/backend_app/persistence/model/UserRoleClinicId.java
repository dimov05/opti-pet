package com.opti_pet.backend_app.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleClinicId implements Serializable {
    private UUID userId;
    private Long roleId;
    private UUID clinicId;
}
