package com.opti_pet.backend_app.rest.request.bill;

import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record BillCreateRequest(@UUID String patientId, Long discountId, Boolean hasInvoice, String note,
                                String billTemplateId) {
}
