package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BilledProcedure;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.response.BilledProcedureResponse;

import java.time.LocalDateTime;

public class BilledProcedureTransformer {

    public static BilledProcedureResponse toResponse(BilledProcedure billedProcedure) {
        return BilledProcedureResponse.builder()
                .id(billedProcedure.getId().toString())
                .name(billedProcedure.getName())
                .billedPrice(billedProcedure.getBilledPrice())
                .taxRatePercent(billedProcedure.getTaxRatePercent())
                .quantity(billedProcedure.getQuantity())
                .discountPercent(billedProcedure.getBill().getDiscount().getPercentProcedures())
                .billedDate(billedProcedure.getBilledDate().toString())
                .billingUserName(billedProcedure.getUser().getName())
                .build();
    }

    public static BilledProcedure toEntity(Long quantity, Procedure procedure, User employee, Clinic clinic) {
        return BilledProcedure.builder()
                .name(procedure.getName())
                .description(procedure.getDescription())
                .billedPrice(procedure.getPrice())
                .taxRatePercent(procedure.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(null)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
