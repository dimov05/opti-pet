package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProcedureTemplateResponse(String procedureId, String name, String description, Long quantity,
                                        BigDecimal finalPrice) {
}
