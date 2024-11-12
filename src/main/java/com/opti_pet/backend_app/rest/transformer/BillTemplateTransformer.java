package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.billTemplate.BillTemplateCreateRequest;
import com.opti_pet.backend_app.rest.response.BillTemplateResponse;

import java.time.LocalDate;
import java.util.ArrayList;

public class BillTemplateTransformer {
    public static BillTemplate toEntity(BillTemplateCreateRequest billTemplateCreateRequest, Clinic clinic, User creator) {
        return BillTemplate.builder()
                .name(billTemplateCreateRequest.name())
                .description(billTemplateCreateRequest.description())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .medicationTemplates(new ArrayList<>())
                .consumableTemplates(new ArrayList<>())
                .procedureTemplates(new ArrayList<>())
                .isActive(true)
                .clinic(clinic)
                .user(creator)
                .build();
    }

    public static BillTemplateResponse toResponse(BillTemplate billTemplate) {
        return BillTemplateResponse.builder()
                .id(billTemplate.getId().toString())
                .name(billTemplate.getName())
                .description(billTemplate.getDescription())
                .creatorName(billTemplate.getUser().getName())
                .dateAdded(billTemplate.getDateAdded().toString())
                .dateUpdated(billTemplate.getDateUpdated().toString())
                .consumables(billTemplate.getConsumableTemplates().stream()
                        .map(ConsumableTemplateTransformer::toResponse)
                        .toList())
                .medications(billTemplate.getMedicationTemplates().stream()
                        .map(MedicationTemplateTransformer::toResponse)
                        .toList())
                .procedures(billTemplate.getProcedureTemplates().stream()
                        .map(ProcedureTemplateTransformer::toResponse)
                        .toList())
                .build();

    }
}
