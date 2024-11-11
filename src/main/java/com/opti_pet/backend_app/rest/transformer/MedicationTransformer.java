package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.rest.request.medication.MedicationCreateRequest;
import com.opti_pet.backend_app.rest.response.MedicationResponse;

import java.time.LocalDate;

public class MedicationTransformer {

    public static MedicationResponse toResponse(Medication medication) {
        return MedicationResponse.builder()
                .id(medication.getId().toString())
                .name(medication.getName())
                .description(medication.getDescription())
                .price(medication.getPrice())
                .availableQuantity(medication.getAvailableQuantity())
                .finalPrice(medication.getFinalPrice())
                .taxRatePercent(medication.getTaxRatePercent())
                .dateAdded(medication.getDateAdded().toString())
                .dateUpdated(medication.getDateUpdated().toString())
                .clinic(ClinicTransformer.toBaseResponse(medication.getClinic()))
                .build();
    }

    public static Medication toEntity(MedicationCreateRequest medicationCreateRequest, Clinic clinic) {
        return Medication.builder()
                .name(medicationCreateRequest.name())
                .description(medicationCreateRequest.description())
                .price(medicationCreateRequest.billedPrice())
                .availableQuantity(medicationCreateRequest.availableQuantity())
                .finalPrice(medicationCreateRequest.finalPrice())
                .taxRatePercent(medicationCreateRequest.taxRatePercent())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
