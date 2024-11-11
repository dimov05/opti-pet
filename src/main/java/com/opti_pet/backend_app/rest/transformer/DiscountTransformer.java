package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Discount;
import com.opti_pet.backend_app.rest.request.discount.DiscountCreateRequest;
import com.opti_pet.backend_app.rest.response.DiscountResponse;

import java.time.LocalDate;

public class DiscountTransformer {

    public static DiscountResponse toResponse(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId().toString())
                .name(discount.getName())
                .percentConsumables(discount.getPercentConsumables())
                .percentHospitals(discount.getPercentHospitals())
                .percentMedications(discount.getPercentMedications())
                .percentProcedures(discount.getPercentProcedures())
                .dateAdded(discount.getDateAdded().toString())
                .dateUpdated(discount.getDateUpdated().toString())
                .clinic(ClinicTransformer.toBaseResponse(discount.getClinic()))
                .build();
    }

    public static Discount toEntity(DiscountCreateRequest discountCreateRequest, Clinic clinic) {
        return Discount.builder()
                .name(discountCreateRequest.name())
                .percentConsumables(discountCreateRequest.percentConsumables())
                .percentHospitals(discountCreateRequest.percentHospitals())
                .percentMedications(discountCreateRequest.percentMedications())
                .percentProcedures(discountCreateRequest.percentProcedures())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
