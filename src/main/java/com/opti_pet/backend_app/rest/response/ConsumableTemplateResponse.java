package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ConsumableTemplateResponse(String consumableId, String name, String description, Long quantity,
                                         BigDecimal finalPrice) {
}
