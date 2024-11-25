package com.opti_pet.backend_app.rest.request.bill;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Builder
public record BillCreateRequest(@UUID String patientId, Long discountId, @PositiveOrZero BigDecimal paidAmount,
                                @NotNull Boolean hasInvoice, String note,
                                String billTemplateId) {
}
