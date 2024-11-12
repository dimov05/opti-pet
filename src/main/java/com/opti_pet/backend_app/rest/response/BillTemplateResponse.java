package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BillTemplateResponse(String id, String name, String description, String dateAdded, String dateUpdated,
                                   String creatorName, List<ConsumableTemplateResponse> consumables,
                                   List<MedicationTemplateResponse> medications,
                                   List<ProcedureTemplateResponse> procedures) {
}
