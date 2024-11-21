package com.opti_pet.backend_app.util.specifications;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import com.opti_pet.backend_app.persistence.model.Clinic;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class BillTemplateSpecifications {
    public static Specification<BillTemplate> billTemplateNameOrDescriptionLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(NAME_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(DESCRIPTION_FIELD_NAME)), keyword)
        ));
    }

    public static Specification<BillTemplate> clinicIdEquals(UUID clinicId) {
        return ((root, query, builder) -> {
            Join<BillTemplate, Clinic> billTemplateClinicJoin = root.join(CLINIC_FIELD_NAME);

            return builder.equal(billTemplateClinicJoin.get(ID_FIELD_NAME), clinicId);
        });
    }
}
