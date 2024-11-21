package com.opti_pet.backend_app.util.specifications;

import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class UserRoleClinicSpecifications {
    public static Specification<UserRoleClinic> userRoleClinicEmployeeNameOrEmailLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> {
            Join<UserRoleClinic, User> userRoleClinicUserJoin = root.join(USER_FIELD_NAME);

            return builder.or(
                    builder.like(builder.lower(userRoleClinicUserJoin.get(NAME_FIELD_NAME)), keyword),
                    builder.like(builder.lower(userRoleClinicUserJoin.get(EMAIL_FIELD_NAME)), keyword)
            );
        });
    }

    public static Specification<UserRoleClinic> clinicIdEquals(UUID clinicId) {
        return ((root, query, builder) -> builder.equal(root.get(CLINIC_ID_FIELD_NAME), clinicId));
    }
}
