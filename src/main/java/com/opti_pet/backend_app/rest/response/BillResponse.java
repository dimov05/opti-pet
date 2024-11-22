package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BillResponse(String id, PatientBaseResponse patient, ClientResponse client,
                           BigDecimal amount,BigDecimal amountBeforeTaxAfterDiscount, BigDecimal amountAfterTax, BigDecimal paidAmount,
                           BigDecimal remainingAmount, boolean hasInvoice, String note, String openDate,
                           String updatedDate, String closeDate, String createdBy, String updatedBy,
                           List<BilledMedicationResponse> billedMedications,
                           List<BilledConsumableResponse> billedConsumables,
                           List<BilledProcedureResponse> billedProcedures,
                           List<BookedHospitalResponse> bookedHospitals) {
}
