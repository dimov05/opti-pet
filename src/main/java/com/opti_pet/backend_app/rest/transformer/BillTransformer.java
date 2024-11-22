package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.request.bill.BillCreateRequest;
import com.opti_pet.backend_app.rest.response.BillResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .openDate(bill.getOpenDate().toString())
                .updatedDate(bill.getUpdatedDate().toString())
                .closeDate(bill.getCloseDate().toString())
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

    public static Bill toEntity(BillCreateRequest billCreateRequest, Clinic clinic, User authenticatedEmployee, Discount discount,
                                Patient patient, List<BilledMedication> billedMedications, List<BilledConsumable> billedConsumables,
                                List<BilledProcedure> billedProcedures, List<BookedHospital> bookedHospitals) {
        final BigDecimal hundred = BigDecimal.valueOf(100L);
        final BigDecimal discountMedications = discount != null ? BigDecimal.ONE.subtract((discount.getPercentMedications().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ZERO;
        final BigDecimal discountProcedures = discount != null ? BigDecimal.ONE.subtract((discount.getPercentProcedures().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ZERO;
        final BigDecimal discountConsumables = discount != null ? BigDecimal.ONE.subtract((discount.getPercentConsumables().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ZERO;
        final BigDecimal discountHospitals = discount != null ? BigDecimal.ONE.subtract((discount.getPercentHospitals().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ZERO;

        BigDecimal[] amountMedicationsBeforeTax = {BigDecimal.ZERO};
        BigDecimal[] amountMedicationsBeforeTaxAndAfterDiscount = {BigDecimal.ZERO};
        BigDecimal[] amountMedicationsAfterTax = {BigDecimal.ZERO};

        BigDecimal[] amountConsumablesBeforeTax = {BigDecimal.ZERO};
        BigDecimal[] amountConsumablesBeforeTaxAndAfterDiscount = {BigDecimal.ZERO};
        BigDecimal[] amountConsumablesAfterTax = {BigDecimal.ZERO};

        BigDecimal[] amountProceduresBeforeTax = {BigDecimal.ZERO};
        BigDecimal[] amountProceduresBeforeTaxAndAfterDiscount = {BigDecimal.ZERO};
        BigDecimal[] amountProceduresAfterTax = {BigDecimal.ZERO};

        BigDecimal[] amountHospitalsBeforeTax = {BigDecimal.ZERO};
        BigDecimal[] amountHospitalsBeforeTaxAndAfterDiscount = {BigDecimal.ZERO};
        BigDecimal[] amountHospitalsAfterTax = {BigDecimal.ZERO};


        billedMedications.forEach(billedMedication -> {
            BigDecimal amountBeforeTax = billedMedication.getBilledPrice().multiply(BigDecimal.valueOf(billedMedication.getQuantity()));
            BigDecimal amountBeforeTaxAfterDiscount = amountBeforeTax.multiply(discountMedications);
            BigDecimal amountAfterTax = amountBeforeTaxAfterDiscount.multiply(BigDecimal.ONE.add(billedMedication.getTaxRatePercent().divide(hundred, 2, RoundingMode.HALF_UP)));
            amountMedicationsBeforeTax[0] = amountMedicationsBeforeTax[0].add(amountBeforeTax);
            amountMedicationsBeforeTaxAndAfterDiscount[0] = amountMedicationsBeforeTaxAndAfterDiscount[0].add(amountBeforeTaxAfterDiscount);
            amountMedicationsAfterTax[0] = amountMedicationsAfterTax[0].add(amountAfterTax);
        });
        billedConsumables.forEach(billedConsumable -> {
            BigDecimal amountBeforeTax = billedConsumable.getBilledPrice().multiply(BigDecimal.valueOf(billedConsumable.getQuantity()));
            BigDecimal amountBeforeTaxAfterDiscount = amountBeforeTax.multiply(discountConsumables);
            BigDecimal amountAfterTax = amountBeforeTaxAfterDiscount.multiply(BigDecimal.ONE.add(billedConsumable.getTaxRatePercent().divide(hundred, 2, RoundingMode.HALF_UP)));
            amountConsumablesBeforeTax[0] = amountConsumablesBeforeTax[0].add(amountBeforeTax);
            amountConsumablesBeforeTaxAndAfterDiscount[0] = amountConsumablesBeforeTaxAndAfterDiscount[0].add(amountBeforeTaxAfterDiscount);
            amountConsumablesAfterTax[0] = amountConsumablesAfterTax[0].add(amountAfterTax);
        });
        billedProcedures.forEach(billedProcedure -> {
            BigDecimal amountBeforeTax = billedProcedure.getBilledPrice().multiply(BigDecimal.valueOf(billedProcedure.getQuantity()));
            BigDecimal amountBeforeTaxAfterDiscount = amountBeforeTax.multiply(discountProcedures);
            BigDecimal amountAfterTax = amountBeforeTaxAfterDiscount.multiply(BigDecimal.ONE.add(billedProcedure.getTaxRatePercent().divide(hundred, 2, RoundingMode.HALF_UP)));
            amountProceduresBeforeTax[0] = amountProceduresBeforeTax[0].add(amountBeforeTax);
            amountProceduresBeforeTaxAndAfterDiscount[0] = amountProceduresBeforeTaxAndAfterDiscount[0].add(amountBeforeTaxAfterDiscount);
            amountProceduresAfterTax[0] = amountProceduresAfterTax[0].add(amountAfterTax);
        });
        bookedHospitals.forEach(bookedHospital -> {
            BigDecimal amountBeforeTax = bookedHospital.getBilledPrice().multiply(BigDecimal.valueOf(bookedHospital.getBookedHours()).divide(BigDecimal.valueOf(24), 0, RoundingMode.HALF_UP));
            BigDecimal amountBeforeTaxAfterDiscount = amountBeforeTax.multiply(discountHospitals);
            BigDecimal amountAfterTax = amountBeforeTax.multiply(BigDecimal.ONE.add(bookedHospital.getTaxRatePercent().divide(hundred, 2, RoundingMode.HALF_UP)));
            amountHospitalsBeforeTax[0] = amountHospitalsBeforeTax[0].add(amountBeforeTax);
            amountHospitalsBeforeTaxAndAfterDiscount[0] = amountHospitalsBeforeTaxAndAfterDiscount[0].add(amountBeforeTaxAfterDiscount);
            amountHospitalsAfterTax[0] = amountHospitalsAfterTax[0].add(amountAfterTax);
        });
        BigDecimal amountBeforeTax = amountConsumablesBeforeTax[0].add(amountMedicationsBeforeTax[0]).add(amountProceduresBeforeTax[0]).add(amountHospitalsBeforeTax[0]);
        BigDecimal amountBeforeTaxAfterDiscount = amountConsumablesBeforeTaxAndAfterDiscount[0].add(amountMedicationsBeforeTaxAndAfterDiscount[0]).add(amountProceduresBeforeTaxAndAfterDiscount[0]).add(amountHospitalsBeforeTaxAndAfterDiscount[0]);
        BigDecimal amountAfterTax = amountConsumablesAfterTax[0].add(amountMedicationsAfterTax[0]).add(amountProceduresAfterTax[0]).add(amountHospitalsAfterTax[0]);

        return Bill.builder()
                .amount(amountBeforeTax)
                .amountBeforeTaxAfterDiscount(amountBeforeTaxAfterDiscount)
                .amountAfterTax(amountAfterTax)
                .paidAmount(BigDecimal.ZERO)
                .discount(discount)
                .remainingAmount(amountAfterTax)
                .hasInvoice(billCreateRequest.hasInvoice())
                .note(billCreateRequest.note())
                .openDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .closeDate(null)
                .createdByUser(authenticatedEmployee)
                .updatedByUser(authenticatedEmployee)
                .patient(patient)
                .clinic(clinic)
                .billedMedications(billedMedications)
                .billedConsumables(billedConsumables)
                .billedProcedures(billedProcedures)
                .bookedHospitals(bookedHospitals)
                .build();
    }
}
