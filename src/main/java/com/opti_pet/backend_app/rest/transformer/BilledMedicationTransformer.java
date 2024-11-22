package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BilledMedication;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.bill.BilledMedicationRequest;
import com.opti_pet.backend_app.rest.response.BilledMedicationResponse;

import java.time.LocalDateTime;

public class BilledMedicationTransformer {

    public static BilledMedicationResponse toResponse(BilledMedication billedMedication) {
        return BilledMedicationResponse.builder()
                .id(billedMedication.getId().toString())
                .name(billedMedication.getName())
                .billedPrice(billedMedication.getBilledPrice())
                .taxRatePercent(billedMedication.getTaxRatePercent())
                .quantity(billedMedication.getQuantity())
                .discountPercent(billedMedication.getBill().getDiscount().getPercentMedications())
                .billedDate(billedMedication.getBilledDate().toString())
                .billingUserName(billedMedication.getUser().getName())
                .build();
    }

    public static BilledMedication toEntity(Long quantity, Medication medication, User employee, Clinic clinic) {
        return BilledMedication.builder()
                .name(medication.getName())
                .description(medication.getDescription())
                .billedPrice(medication.getPrice())
                .taxRatePercent(medication.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(null)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
