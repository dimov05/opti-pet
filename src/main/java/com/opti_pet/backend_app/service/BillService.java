package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.persistence.repository.*;
import com.opti_pet.backend_app.rest.request.bill.*;
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

import java.util.ArrayList;
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
    private final BilledMedicationRepository billedMedicationRepository;
    private final BilledConsumableRepository billedConsumableRepository;
    private final BilledProcedureRepository billedProcedureRepository;
    private final BookedHospitalRepository bookedHospitalRepository;
    private final MedicationService medicationService;
    private final ConsumableService consumableService;
    private final ProcedureService procedureService;
    private final HospitalService hospitalService;

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
        List<BilledMedication> billedMedications = getBilledMedications(billCreateRequest.billedMedications(), authenticatedEmployee, clinic);
        List<BilledConsumable> billedConsumables = getBilledConsumables(billCreateRequest.billedConsumables(), authenticatedEmployee, clinic);
        List<BilledProcedure> billedProcedures = getBilledProcedures(billCreateRequest.billedProcedures(), authenticatedEmployee, clinic);
        List<BookedHospital> bookedHospitals = getBookedHospitals(billCreateRequest.bookedHospitals(), authenticatedEmployee, clinic, patient);
        Bill bill = BillTransformer.toEntity(billCreateRequest, clinic, authenticatedEmployee, discount, patient, billedMedications, billedConsumables, billedProcedures, bookedHospitals);

        billedMedications.forEach(m -> m.setBill(bill));
        billedConsumables.forEach(c -> c.setBill(bill));
        billedProcedures.forEach(p -> p.setBill(bill));
        bookedHospitals.forEach(h -> h.setBill(bill));
        billedMedicationRepository.saveAll(billedMedications);
        billedConsumableRepository.saveAll(billedConsumables);
        billedProcedureRepository.saveAll(billedProcedures);
        bookedHospitalRepository.saveAll(bookedHospitals);

        return BillTransformer.toResponse(billRepository.save(bill));
    }

    private List<BilledMedication> getBilledMedications(List<BilledMedicationRequest> billedMedicationRequests, User employee, Clinic clinic) {
        List<BilledMedication> billedMedications = new ArrayList<>();
        billedMedicationRequests.forEach(billedMedicationRequest -> {
            Medication medication = medicationService.getMedicationByIdOrThrowException(UUID.fromString(billedMedicationRequest.medicationId()));
            BilledMedication billedMedication = billedMedicationRepository.save(BilledMedicationTransformer.toEntity(billedMedicationRequest.quantity(), medication, employee, clinic));
            billedMedications.add(billedMedication);
        });

        return billedMedications;
    }

    private List<BilledConsumable> getBilledConsumables(List<BilledConsumableRequest> billedConsumableRequests, User employee, Clinic clinic) {
        List<BilledConsumable> billedConsumables = new ArrayList<>();
        billedConsumableRequests.forEach(billedConsumableRequest -> {
            Consumable consumable = consumableService.getConsumableByIdOrThrowException(UUID.fromString(billedConsumableRequest.consumableId()));
            BilledConsumable billedConsumable = billedConsumableRepository.save(BilledConsumableTransformer.toEntity(billedConsumableRequest.quantity(), consumable, employee, clinic));
            billedConsumables.add(billedConsumable);
        });

        return billedConsumables;
    }

    private List<BilledProcedure> getBilledProcedures(List<BilledProcedureRequest> billedProcedureRequests, User employee, Clinic clinic) {
        List<BilledProcedure> billedProcedures = new ArrayList<>();
        billedProcedureRequests.forEach(billedProcedureRequest -> {
            Procedure procedure = procedureService.getProcedureByIdOrThrowException(UUID.fromString(billedProcedureRequest.procedureId()));
            BilledProcedure billedProcedure = billedProcedureRepository.save(BilledProcedureTransformer.toEntity(billedProcedureRequest.quantity(), procedure, employee, clinic));
            billedProcedures.add(billedProcedure);
        });

        return billedProcedures;
    }

    private List<BookedHospital> getBookedHospitals(List<BookedHospitalRequest> bookedHospitalRequests, User employee, Clinic clinic, Patient patient) {
        List<BookedHospital> bookedHospitals = new ArrayList<>();
        bookedHospitalRequests.forEach(bookedHospitalRequest -> {
            Hospital hospital = hospitalService.getHospitalByIdOrThrowException(UUID.fromString(bookedHospitalRequest.hospitalId()));
            BookedHospital bookedHospital = bookedHospitalRepository.save(BookedHospitalTransformer.toEntity(hospital, employee, clinic, patient));
            bookedHospitals.add(bookedHospital);
        });

        return bookedHospitals;
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
}
