package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.persistence.model.MedicationTemplate;
import com.opti_pet.backend_app.rest.response.MedicationTemplateResponse;

public class MedicationTemplateTransformer {

    public static MedicationTemplate toEntity(Long quantity, Medication medication, BillTemplate billTemplate) {
        return MedicationTemplate.builder()
                .quantity(quantity)
                .billTemplate(billTemplate)
                .medication(medication)
                .build();
    }

    public static MedicationTemplateResponse toResponse(MedicationTemplate medicationTemplate) {
        Medication medication = medicationTemplate.getMedication();
        return MedicationTemplateResponse.builder()
                .medicationId(medication.getId().toString())
                .name(medication.getName())
                .description(medication.getDescription())
                .quantity(medicationTemplate.getQuantity())
                .finalPrice(medication.getFinalPrice())
                .build();
    }
}
