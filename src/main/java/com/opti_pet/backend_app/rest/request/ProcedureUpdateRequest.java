package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Builder
public record ProcedureUpdateRequest(@UUID String procedureId, @NotBlank String name,
                                     @Length(max = 255) String description, @NotNull BigDecimal billedPrice,
                                     @NotNull BigDecimal taxRatePercent, @NotNull BigDecimal finalPrice) {
}