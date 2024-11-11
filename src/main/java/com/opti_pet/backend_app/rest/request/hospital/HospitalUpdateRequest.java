package com.opti_pet.backend_app.rest.request.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Builder
public record HospitalUpdateRequest(@UUID String hospitalId, @NotBlank String name,
                                    @Length(max = 255) String description,
                                    @NotNull @PositiveOrZero BigDecimal billedPrice,
                                    @NotNull @PositiveOrZero BigDecimal taxRatePercent,
                                    @NotNull @PositiveOrZero BigDecimal finalPrice) {
}
