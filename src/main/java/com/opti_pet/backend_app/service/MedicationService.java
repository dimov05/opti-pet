package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Medication;
import com.opti_pet.backend_app.persistence.repository.MedicationRepository;
import com.opti_pet.backend_app.rest.request.medication.MedicationCreateRequest;
import com.opti_pet.backend_app.rest.request.medication.MedicationUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.ExtendedSpecificationRequest;
import com.opti_pet.backend_app.rest.response.MedicationResponse;
import com.opti_pet.backend_app.rest.transformer.MedicationTransformer;
import com.opti_pet.backend_app.util.specifications.MedicationSpecifications;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.*;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final ClinicService clinicService;

    @Transactional
    public MedicationResponse createMedication(String clinicId, MedicationCreateRequest medicationCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        checkIfPriceIsCorrect(medicationCreateRequest);
        Medication medication = medicationRepository.save(MedicationTransformer.toEntity(medicationCreateRequest, clinic));

        return MedicationTransformer.toResponse(medication);
    }

    @Transactional
    public MedicationResponse updateMedication(MedicationUpdateRequest medicationUpdateRequest) {
        Medication medication = getMedicationByIdOrThrowException(UUID.fromString(medicationUpdateRequest.medicationId()));
        checkIfPriceIsCorrect(medicationUpdateRequest);
        updateMedicationField(medicationUpdateRequest::name, medication::getName, medication::setName);
        updateMedicationField(medicationUpdateRequest::description, medication::getDescription, medication::setDescription);
        if (!medicationUpdateRequest.billedPrice().equals(medication.getPrice())) {
            medication.setPrice(medicationUpdateRequest.billedPrice());
        }
        if (!medicationUpdateRequest.finalPrice().equals(medication.getFinalPrice())) {
            medication.setFinalPrice(medicationUpdateRequest.finalPrice());
        }
        if (!medicationUpdateRequest.taxRatePercent().equals(medication.getTaxRatePercent())) {
            medication.setTaxRatePercent(medicationUpdateRequest.taxRatePercent());
        }
        if (!medicationUpdateRequest.availableQuantity().equals(medication.getAvailableQuantity())) {
            medication.setAvailableQuantity(medicationUpdateRequest.availableQuantity());
        }

        medication.setDateUpdated(LocalDate.now());

        return MedicationTransformer.toResponse(medicationRepository.save(medication));
    }

    @Transactional
    public MedicationResponse deleteMedicationById(String medicationId) {
        Medication medication = getMedicationByIdOrThrowException(UUID.fromString(medicationId));
        medicationRepository.delete(medication);

        return MedicationTransformer.toResponse(medication);
    }

    @Transactional
    public List<MedicationResponse> getAllMedicationsByClinicIdState(String clinicId) {
        return medicationRepository.findAllByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(MedicationTransformer::toResponse)
                .toList();
    }

    @Transactional
    public Page<MedicationResponse> getAllMedicationsByClinicIdForManager(String clinicId, ExtendedSpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return medicationRepository.findAll(getSpecifications(specificationRequest, clinic), pageRequest)
                .map(MedicationTransformer::toResponse);
    }

    private void updateMedicationField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private Specification<Medication> getSpecifications(ExtendedSpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<Medication> specification = MedicationSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(MedicationSpecifications.medicationNameOrDescriptionLike(inputText));
        }

        return specification;
    }

    public Medication getMedicationByIdOrThrowException(UUID medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, medicationId.toString()));
    }

    private Pageable createPageRequest(ExtendedSpecificationRequest specificationRequest) {
        Sort sort = Sort.unsorted();

        sort = specificationRequest.sortByAvailableQuantity() != null ? sort.and(getSort(specificationRequest.sortByAvailableQuantity(), AVAILABLE_QUANTITY_FIELD_NAME)) : sort;
        sort = specificationRequest.sortByAmount() != null ? sort.and(getSort(specificationRequest.sortByAmount(), PRICE_FIELD_NAME)) : sort;

        int pageNumber = specificationRequest.pageNumber() != null ? specificationRequest.pageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = specificationRequest.pageSize() != null ? specificationRequest.pageSize() : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private Sort getSort(Boolean flag, String fieldName) {
        return Sort.by(Boolean.TRUE.equals(flag) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
    }


    private void checkIfPriceIsCorrect(MedicationCreateRequest medicationCreateRequest) {
        checkBigDecimalPrices(medicationCreateRequest.billedPrice(), medicationCreateRequest.finalPrice(), medicationCreateRequest.taxRatePercent());
    }

    private void checkIfPriceIsCorrect(MedicationUpdateRequest medicationUpdateRequest) {
        checkBigDecimalPrices(medicationUpdateRequest.billedPrice(), medicationUpdateRequest.finalPrice(), medicationUpdateRequest.taxRatePercent());
    }

    public void checkBigDecimalPrices(BigDecimal priceBeforeTax, BigDecimal priceAfterTax, BigDecimal taxPercent) {
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal calculatedPriceAfterTax = priceBeforeTax.multiply(taxRateMultiplier).setScale(2, RoundingMode.HALF_UP);
        if (calculatedPriceAfterTax.compareTo(priceAfterTax) != 0) {
            throw new BadRequestException("Prices are not correct!");
        }
    }
}
