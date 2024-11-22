package com.opti_pet.backend_app.rest.request.bill;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record BookedHospitalRequest(@UUID String hospitalId, @NotBlank String startDate, String endDate) {
}
