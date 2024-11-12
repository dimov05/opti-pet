package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.model.ProcedureTemplate;
import com.opti_pet.backend_app.rest.response.ProcedureTemplateResponse;

public class ProcedureTemplateTransformer {

    public static ProcedureTemplate toEntity(Long quantity, Procedure procedure, BillTemplate billTemplate) {
        return ProcedureTemplate.builder()
                .quantity(quantity)
                .billTemplate(billTemplate)
                .procedure(procedure)
                .build();
    }

    public static ProcedureTemplateResponse toResponse(ProcedureTemplate procedureTemplate) {
        Procedure procedure = procedureTemplate.getProcedure();
        return ProcedureTemplateResponse.builder()
                .procedureId(procedure.getId().toString())
                .name(procedure.getName())
                .description(procedure.getDescription())
                .quantity(procedureTemplate.getQuantity())
                .finalPrice(procedure.getFinalPrice())
                .build();
    }
}
