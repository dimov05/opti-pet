package com.opti_pet.backend_app.util.specifications;

import com.opti_pet.backend_app.persistence.model.Bill;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Patient;
import com.opti_pet.backend_app.persistence.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;

public class BillSpecifications {
    public static Specification<Bill> clinicIdEquals(UUID clinicId) {
        return ((root, query, builder) -> {
            Join<Bill, Clinic> billTemplateClinicJoin = root.join(CLINIC_FIELD_NAME);

            return builder.equal(billTemplateClinicJoin.get(ID_FIELD_NAME), clinicId);
        });
    }

    public static Specification<Bill> billTemplatePatientNameOrPassportNumberOrClientPhoneNumberOrNameLike(String value) {
        String keyword = "%" + value.toLowerCase() + "%";

        return ((root, query, builder) -> {
            Join<Bill, Patient> billPatientJoin = root.join(PATIENT_FIELD_NAME);
            Join<Patient, User> patientOwnerJoin = billPatientJoin.join(OWNER_FIELD_NAME);

            return builder.or(
                    builder.like(builder.lower(billPatientJoin.get(NAME_FIELD_NAME)), keyword),
                    builder.like(builder.lower(billPatientJoin.get(PASSPORT_FIELD_NAME)), keyword),
                    builder.like(builder.lower(patientOwnerJoin.get(NAME_FIELD_NAME)), keyword),
                    builder.like(builder.lower(patientOwnerJoin.get(PHONE_NUMBER_FIELD_NAME)), keyword)
            );
        });
    }
}
