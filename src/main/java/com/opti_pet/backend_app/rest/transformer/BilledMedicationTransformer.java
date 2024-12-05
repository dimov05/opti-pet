package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BilledMedicationResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.opti_pet.backend_app.util.AppConstants.DATE_FORMATTER;

public class BilledMedicationTransformer {

    public static BilledMedicationResponse toResponse(BilledMedication billedMedication) {
        return BilledMedicationResponse.builder()
                .id(billedMedication.getId().toString())
                .name(billedMedication.getName())
                .billedPrice(billedMedication.getBilledPrice())
                .taxRatePercent(billedMedication.getTaxRatePercent())
                .quantity(billedMedication.getQuantity())
                .discountPercent(billedMedication.getBill().getDiscount() != null
                        ? billedMedication.getBill().getDiscount().getPercentMedications()
                        : BigDecimal.ZERO)
                .billedDate(billedMedication.getBilledDate().format(DATE_FORMATTER))
                .billingUserName(billedMedication.getUser().getName())
                .build();
    }

    public static BilledMedication toEntity(Long quantity, Medication medication, User employee, Clinic clinic, Bill bill) {
        return BilledMedication.builder()
                .name(medication.getName())
                .description(medication.getDescription())
                .billedPrice(medication.getPrice())
                .taxRatePercent(medication.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(bill)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
