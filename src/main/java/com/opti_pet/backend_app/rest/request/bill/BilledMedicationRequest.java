package com.opti_pet.backend_app.rest.request.bill;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record BilledMedicationRequest(@UUID String medicationId, @PositiveOrZero Long quantity) {
}
