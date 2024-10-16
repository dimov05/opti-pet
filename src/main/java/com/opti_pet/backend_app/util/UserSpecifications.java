package com.opti_pet.backend_app.util;

import com.opti_pet.backend_app.persistence.model.User;
import org.springframework.data.jpa.domain.Specification;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class UserSpecifications {
    public static Specification<User> userEmailOrFullNameOrPhoneNumberLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(EMAIL_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(NAME_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(PHONE_NUMBER_FIELD_NAME)), keyword)
        ));
    }
}
