package com.opti_pet.backend_app.rest.request.billTemplate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record ConsumableTemplateRequest(@UUID String id, @NotNull @PositiveOrZero Long quantity) {
}
