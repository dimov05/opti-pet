package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountResponse(Long id, String name, BigDecimal percentConsumables, BigDecimal percentMedications,
                               BigDecimal percentHospitals, BigDecimal percentProcedures, String dateAdded,
                               String dateUpdated, ClinicBaseResponse clinic) {
}
