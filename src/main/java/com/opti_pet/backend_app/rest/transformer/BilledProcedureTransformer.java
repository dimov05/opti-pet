package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BilledProcedureResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BilledProcedureTransformer {

    public static BilledProcedureResponse toResponse(BilledProcedure billedProcedure) {
        return BilledProcedureResponse.builder()
                .id(billedProcedure.getId().toString())
                .name(billedProcedure.getName())
                .billedPrice(billedProcedure.getBilledPrice())
                .taxRatePercent(billedProcedure.getTaxRatePercent())
                .quantity(billedProcedure.getQuantity())
                .discountPercent(billedProcedure.getBill().getDiscount() != null
                        ? billedProcedure.getBill().getDiscount().getPercentMedications()
                        : BigDecimal.ZERO)
                .billedDate(billedProcedure.getBilledDate().toString())
                .billingUserName(billedProcedure.getUser().getName())
                .build();
    }

    public static BilledProcedure toEntity(Long quantity, Procedure procedure, User employee, Clinic clinic, Bill bill) {
        return BilledProcedure.builder()
                .name(procedure.getName())
                .description(procedure.getDescription())
                .billedPrice(procedure.getPrice())
                .taxRatePercent(procedure.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(bill)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
