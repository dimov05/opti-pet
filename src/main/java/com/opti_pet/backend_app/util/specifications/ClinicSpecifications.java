package com.opti_pet.backend_app.util.specifications;

import com.opti_pet.backend_app.persistence.model.Clinic;
import org.springframework.data.jpa.domain.Specification;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class ClinicSpecifications {
    public static Specification<Clinic> clinicEmailOrNameOrCityOrAddressOrPhoneNumberLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(EMAIL_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(NAME_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(PHONE_NUMBER_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(CITY_FIELD_NAME)), keyword),
                builder.like(builder.lower(root.get(ADDRESS_FIELD_NAME)), keyword)
        ));
    }

    public static Specification<Clinic> clinicIsNotDefaultClinic() {
        return ((root, query, builder) -> builder.notEqual(root.get(ID_FIELD_NAME), DEFAULT_CLINIC_UUID));
    }
}
