package com.opti_pet.backend_app.util.specifications;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Discount;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class DiscountSpecifications {
    public static Specification<Discount> discountNameLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> builder.like(builder.lower(root.get(NAME_FIELD_NAME)), keyword));
    }

    public static Specification<Discount> clinicIdEquals(UUID clinicId) {
        return ((root, query, builder) -> {
            Join<Discount, Clinic> discountClinicJoin = root.join(CLINIC_FIELD_NAME);

            return builder.equal(discountClinicJoin.get(ID_FIELD_NAME), clinicId);
        });
    }
}
