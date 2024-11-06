package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.rest.request.ProcedureCreateRequest;
import com.opti_pet.backend_app.rest.response.ProcedureResponse;

import java.time.LocalDate;

public class ProcedureTransformer {

    public static ProcedureResponse toResponse(Procedure procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId().toString())
                .name(procedure.getName())
                .description(procedure.getDescription())
                .price(procedure.getPrice())
                .taxRatePercent(procedure.getTaxRatePercent())
                .dateAdded(procedure.getDateAdded().toString())
                .dateUpdated(procedure.getDateUpdated().toString())
                .clinic(ClinicTransformer.toBaseResponse(procedure.getClinic()))
                .build();
    }

    //LocalDate.parse(procedureCreateRequest.birthdate(), DATE_TIME_FORMATTER)
    public static Procedure toEntity(ProcedureCreateRequest procedureCreateRequest, Clinic clinic) {
        return Procedure.builder()
                .name(procedureCreateRequest.name())
                .description(procedureCreateRequest.description())
                .price(procedureCreateRequest.billedPrice())
                .taxRatePercent(procedureCreateRequest.taxRatePercent())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
