package com.opti_pet.backend_app.rest.request.billTemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record ConsumableTemplateRequest(@UUID String id, @NotBlank String name, @NotNull @PositiveOrZero Long quantity) {
}
