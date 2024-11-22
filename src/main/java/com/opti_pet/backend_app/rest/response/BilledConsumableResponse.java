package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BilledConsumableResponse(String id, String name, BigDecimal billedPrice, BigDecimal taxRatePercent,
                                       Long quantity, BigDecimal discountPercent, String billedDate,
                                       String billingUserName) {
}
