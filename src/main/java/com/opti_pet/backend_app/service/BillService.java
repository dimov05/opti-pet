package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.persistence.repository.*;
import com.opti_pet.backend_app.rest.request.bill.BillCreateRequest;
import com.opti_pet.backend_app.rest.request.specification.BillSpecificationRequest;
import com.opti_pet.backend_app.rest.response.BillResponse;
import com.opti_pet.backend_app.rest.transformer.*;
import com.opti_pet.backend_app.util.specifications.BillSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.*;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class BillService {
    private final ClinicService clinicService;
    private final BillRepository billRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final DiscountRepository discountRepository;
    private final PatientRepository patientRepository;
    private final BillTemplateService billTemplateService;

    @Transactional
    public Page<BillResponse> getAllBillsByClinicIdForManager(String clinicId, BillSpecificationRequest billSpecificationRequest) {
        Pageable pageRequest = createPageRequest(billSpecificationRequest);
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return billRepository.findAll(getSpecifications(billSpecificationRequest, clinic), pageRequest)
                .map(BillTransformer::toResponse);
    }

    @Transactional
    public BillResponse createBill(String clinicId, BillCreateRequest billCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User authenticatedEmployee = userService.getUserByEmailOrThrowException(jwtService.extractEmailFromToken());
        Discount discount = billCreateRequest.discountId() != null ? discountRepository.findById(billCreateRequest.discountId()).orElse(null) : null;
        Patient patient = patientRepository.findById(UUID.fromString(billCreateRequest.patientId())).orElseThrow(() -> new BadRequestException("Patient not found"));
        UUID billTemplateId = billCreateRequest.billTemplateId() != null ? tryParseUUID(billCreateRequest.billTemplateId()) : null;

        Bill bill = billRepository.save(BillTransformer.toEntity(billCreateRequest, clinic, authenticatedEmployee, patient, discount));
        if (billTemplateId != null) {
            BillTemplate billTemplate = billTemplateService.getBillTemplateByIdOrThrowException(billTemplateId);
            if (!billTemplate.getMedicationTemplates().isEmpty()) {
                for (MedicationTemplate medicationTemplate : billTemplate.getMedicationTemplates()) {
                    BilledMedication billedMedication = BilledMedicationTransformer.toEntity(medicationTemplate.getQuantity(), medicationTemplate.getMedication(), authenticatedEmployee, clinic, bill);
                    bill.addBilledMedication(billedMedication);
                }
            }
            if (!billTemplate.getConsumableTemplates().isEmpty()) {
                for (ConsumableTemplate consumableTemplate : billTemplate.getConsumableTemplates()) {
                    BilledConsumable billedConsumable = BilledConsumableTransformer.toEntity(consumableTemplate.getQuantity(), consumableTemplate.getConsumable(), authenticatedEmployee, clinic, bill);
                    bill.addBilledConsumable(billedConsumable);
                }
            }
            if (!billTemplate.getProcedureTemplates().isEmpty()) {
                for (ProcedureTemplate procedureTemplate : billTemplate.getProcedureTemplates()) {
                    BilledProcedure billedProcedure = BilledProcedureTransformer.toEntity(procedureTemplate.getQuantity(), procedureTemplate.getProcedure(), authenticatedEmployee, clinic, bill);
                    bill.addBilledProcedure(billedProcedure);
                }
            }
        }

        bill = billRepository.save(bill);
        setBillPriceAndItems(bill);

        return BillTransformer.toResponse(billRepository.save(bill));
    }

    private void setBillPriceAndItems(Bill bill) {
        List<BilledMedication> billedMedications = bill.getBilledMedications();
        List<BilledConsumable> billedConsumables = bill.getBilledConsumables();
        List<BilledProcedure> billedProcedures = bill.getBilledProcedures();
        List<BookedHospital> bookedHospitals = bill.getBookedHospitals();
        Discount discount = bill.getDiscount();

        final BigDecimal hundred = BigDecimal.valueOf(100L);
        final BigDecimal discountMedications = discount != null ? BigDecimal.ONE.subtract((discount.getPercentMedications().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ONE;
        final BigDecimal discountProcedures = discount != null ? BigDecimal.ONE.subtract((discount.getPercentProcedures().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ONE;
        final BigDecimal discountConsumables = discount != null ? BigDecimal.ONE.subtract((discount.getPercentConsumables().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ONE;
        final BigDecimal discountHospitals = discount != null ? BigDecimal.ONE.subtract((discount.getPercentHospitals().divide(hundred, 2, RoundingMode.HALF_UP))) : BigDecimal.ONE;

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

        bill.setAmount(amountBeforeTax);
        bill.setAmountAfterTax(amountAfterTax);
        bill.setAmountBeforeTaxAfterDiscount(amountBeforeTaxAfterDiscount);
        bill.setRemainingAmount(amountAfterTax);

    }

    private Pageable createPageRequest(BillSpecificationRequest specificationRequest) {
        int pageNumber = specificationRequest.pageNumber() != null ? specificationRequest.pageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = specificationRequest.pageSize() != null ? specificationRequest.pageSize() : DEFAULT_PAGE_SIZE;
        Sort sort = Sort.unsorted();

        if (specificationRequest.sortByRemainingAmount() == null && specificationRequest.sortByAmountAfterTax() == null) {
            sort = getSort(false, OPEN_DATE_FIELD_NAME);
            return PageRequest.of(pageNumber, pageSize, sort);
        }
        sort = specificationRequest.sortByAmountAfterTax() != null ? sort.and(getSort(specificationRequest.sortByAmountAfterTax(), AMOUNT_AFTER_TAX_FIELD_NAME)) : sort;
        sort = specificationRequest.sortByRemainingAmount() != null ? sort.and(getSort(specificationRequest.sortByRemainingAmount(), REMAINING_AMOUNT_FIELD_NAME)) : sort;

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private Sort getSort(Boolean flag, String fieldName) {
        return Sort.by(Boolean.TRUE.equals(flag) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
    }

    private Specification<Bill> getSpecifications(BillSpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<Bill> specification = BillSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(BillSpecifications.billTemplatePatientNameOrPassportNumberOrClientPhoneNumberOrNameLike(inputText));
        }

        return specification;
    }

    private static UUID tryParseUUID(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
