package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BookedHospitalResponse(String id, String name, BigDecimal billedPrice, BigDecimal taxRatePercent,
                                     Long bookedHours, String startDate, String endDate, String billedDate,
                                     BigDecimal discountPercent, String billingUserName) {
}
