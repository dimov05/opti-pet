package com.opti_pet.backend_app.util;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Medication;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class MedicationSpecifications {
    public static Specification<Medication> medicationNameOrDescriptionLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(NAME_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(DESCRIPTION_FIELD_NAME)), keyword)
        ));
    }

    public static Specification<Medication> clinicIdEquals(UUID clinicId) {
        return ((root, query, builder) -> {
            Join<Medication, Clinic> medicationClinicJoin = root.join(CLINIC_FIELD_NAME);

            return builder.equal(medicationClinicJoin.get(ID_FIELD_NAME), clinicId);
        });
    }
}
