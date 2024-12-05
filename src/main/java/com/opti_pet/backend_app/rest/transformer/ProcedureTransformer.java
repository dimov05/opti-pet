package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.rest.request.procedure.ProcedureCreateRequest;
import com.opti_pet.backend_app.rest.response.ProcedureResponse;

import java.time.LocalDate;

import static com.opti_pet.backend_app.util.AppConstants.DATE_FORMATTER;

public class ProcedureTransformer {

    public static ProcedureResponse toResponse(Procedure procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId().toString())
                .name(procedure.getName())
                .description(procedure.getDescription())
                .price(procedure.getPrice())
                .finalPrice(procedure.getFinalPrice())
                .taxRatePercent(procedure.getTaxRatePercent())
                .dateAdded(procedure.getDateAdded().format(DATE_FORMATTER))
                .dateUpdated(procedure.getDateUpdated().format(DATE_FORMATTER))
                .clinic(ClinicTransformer.toBaseResponse(procedure.getClinic()))
                .build();
    }

    public static Procedure toEntity(ProcedureCreateRequest procedureCreateRequest, Clinic clinic) {
        return Procedure.builder()
                .name(procedureCreateRequest.name())
                .description(procedureCreateRequest.description())
                .price(procedureCreateRequest.billedPrice())
                .finalPrice(procedureCreateRequest.finalPrice())
                .taxRatePercent(procedureCreateRequest.taxRatePercent())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
