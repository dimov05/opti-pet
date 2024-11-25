package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.request.bill.BillCreateRequest;
import com.opti_pet.backend_app.rest.response.BillResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.opti_pet.backend_app.util.AppConstants.DATE_TIME_FORMATTER;

public class BillTransformer {
    public static BillResponse toResponse(Bill bill) {
        return BillResponse.builder()
                .id(bill.getId().toString())
                .patient(PatientTransformer.toBaseResponse(bill.getPatient()))
                .client(UserTransformer.toClientResponse(bill.getPatient().getOwner()))
                .amount(bill.getAmount())
                .amountBeforeTaxAfterDiscount(bill.getAmountBeforeTaxAfterDiscount())
                .amountAfterTax(bill.getAmountAfterTax())
                .paidAmount(bill.getPaidAmount())
                .remainingAmount(bill.getRemainingAmount())
                .hasInvoice(bill.isHasInvoice())
                .note(bill.getNote())
                .openDate(bill.getOpenDate().format(DATE_TIME_FORMATTER))
                .updatedDate(bill.getUpdatedDate().format(DATE_TIME_FORMATTER))
                .closeDate(bill.getCloseDate() != null ? bill.getCloseDate().format(DATE_TIME_FORMATTER) : null)
                .createdBy(bill.getCreatedByUser().getName())
                .updatedBy(bill.getUpdatedByUser().getName())
                .billedMedications(bill.getBilledMedications() != null ? bill.getBilledMedications().stream()
                        .map(BilledMedicationTransformer::toResponse)
                        .toList() : new ArrayList<>())
                .billedConsumables(bill.getBilledConsumables() != null ? bill.getBilledConsumables().stream()
                        .map(BilledConsumableTransformer::toResponse)
                        .toList() : new ArrayList<>())
                .billedProcedures(bill.getBilledProcedures() != null ? bill.getBilledProcedures().stream()
                        .map(BilledProcedureTransformer::toResponse)
                        .toList() : new ArrayList<>())
                .bookedHospitals(bill.getBookedHospitals() != null ? bill.getBookedHospitals().stream()
                        .map(BookedHospitalTransformer::toResponse)
                        .toList() : new ArrayList<>())
                .build();

    }

    public static Bill toEntity(BillCreateRequest billCreateRequest, Clinic clinic, User authenticatedEmployee,
                                Patient patient, Discount discount) {
        return Bill.builder()
                .amount(null)
                .amountBeforeTaxAfterDiscount(null)
                .amountAfterTax(null)
                .paidAmount(BigDecimal.ZERO)
                .discount(discount)
                .remainingAmount(null)
                .hasInvoice(billCreateRequest.hasInvoice())
                .note(billCreateRequest.note())
                .openDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .closeDate(null)
                .createdByUser(authenticatedEmployee)
                .updatedByUser(authenticatedEmployee)
                .patient(patient)
                .clinic(clinic)
                .billedMedications(new ArrayList<>())
                .billedConsumables(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .bookedHospitals(new ArrayList<>())
                .build();
    }
}
