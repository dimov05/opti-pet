package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record HospitalResponse(String id, String name, String description, BigDecimal price, BigDecimal taxRatePercent,
                               BigDecimal finalPrice, String dateAdded, String dateUpdated, ClinicBaseResponse clinic) {
}
