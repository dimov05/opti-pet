package com.opti_pet.backend_app.rest.request.bill;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record BilledConsumableRequest(@UUID String consumableId, @PositiveOrZero Long quantity) {
}
