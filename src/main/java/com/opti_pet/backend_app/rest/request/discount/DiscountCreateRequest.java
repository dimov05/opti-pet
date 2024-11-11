package com.opti_pet.backend_app.rest.request.discount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DiscountCreateRequest(@NotBlank String name, @NotNull @PositiveOrZero BigDecimal percentConsumables,
                                    @NotNull @PositiveOrZero BigDecimal percentProcedures,
                                    @NotNull @PositiveOrZero BigDecimal percentMedications,
                                    @NotNull @PositiveOrZero BigDecimal percentHospitals) {
}
