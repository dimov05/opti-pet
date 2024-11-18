package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Procedure;
import com.opti_pet.backend_app.persistence.repository.ProcedureRepository;
import com.opti_pet.backend_app.rest.request.procedure.ProcedureCreateRequest;
import com.opti_pet.backend_app.rest.request.procedure.ProcedureUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.SpecificationRequest;
import com.opti_pet.backend_app.rest.response.ProcedureResponse;
import com.opti_pet.backend_app.rest.transformer.ProcedureTransformer;
import com.opti_pet.backend_app.util.specifications.ProcedureSpecifications;
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
public class ProcedureService {
    private final ProcedureRepository procedureRepository;
    private final ClinicService clinicService;

    @Transactional
    public ProcedureResponse createProcedure(String clinicId, ProcedureCreateRequest procedureCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        checkIfPriceIsCorrect(procedureCreateRequest);
        Procedure procedure = procedureRepository.save(ProcedureTransformer.toEntity(procedureCreateRequest, clinic));

        return ProcedureTransformer.toResponse(procedure);
    }

    @Transactional
    public ProcedureResponse updateProcedure(ProcedureUpdateRequest procedureUpdateRequest) {
        Procedure procedure = getProcedureByIdOrThrowException(UUID.fromString(procedureUpdateRequest.procedureId()));
        checkIfPriceIsCorrect(procedureUpdateRequest);
        updateProcedureField(procedureUpdateRequest::name, procedure::getName, procedure::setName);
        updateProcedureField(procedureUpdateRequest::description, procedure::getDescription, procedure::setDescription);
        if (!procedureUpdateRequest.billedPrice().equals(procedure.getPrice())) {
            procedure.setPrice(procedureUpdateRequest.billedPrice());
        }
        if (!procedureUpdateRequest.finalPrice().equals(procedure.getFinalPrice())) {
            procedure.setFinalPrice(procedureUpdateRequest.finalPrice());
        }
        if (!procedureUpdateRequest.taxRatePercent().equals(procedure.getTaxRatePercent())) {
            procedure.setTaxRatePercent(procedureUpdateRequest.taxRatePercent());
        }

        procedure.setDateUpdated(LocalDate.now());

        return ProcedureTransformer.toResponse(procedureRepository.save(procedure));
    }

    @Transactional
    public ProcedureResponse deleteProcedureById(String procedureId) {
        Procedure procedure = getProcedureByIdOrThrowException(UUID.fromString(procedureId));
        procedureRepository.delete(procedure);

        return ProcedureTransformer.toResponse(procedure);
    }

    @Transactional
    public List<ProcedureResponse> getAllProceduresByClinicIdState(String clinicId) {
        return procedureRepository.findAllByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(ProcedureTransformer::toResponse)
                .toList();
    }

    @Transactional
    public Page<ProcedureResponse> getAllProceduresByClinicIdForManager(String clinicId, SpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return procedureRepository.findAll(getSpecifications(specificationRequest, clinic), pageRequest)
                .map(ProcedureTransformer::toResponse);
    }

    private void updateProcedureField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private Specification<Procedure> getSpecifications(SpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<Procedure> specification = ProcedureSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(ProcedureSpecifications.procedureNameOrDescriptionLike(inputText));
        }

        return specification;
    }

    private Procedure getProcedureByIdOrThrowException(UUID procedureId) {
        return procedureRepository.findById(procedureId)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, procedureId.toString()));
    }

    private Pageable createPageRequest(SpecificationRequest specificationRequest) {
        Sort sort = Sort.unsorted();
        sort = specificationRequest.sortByAmount() != null ? sort.and(getSort(specificationRequest.sortByAmount(), PRICE_FIELD_NAME)) : sort;

        int pageNumber = specificationRequest.pageNumber() != null ? specificationRequest.pageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = specificationRequest.pageSize() != null ? specificationRequest.pageSize() : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private Sort getSort(Boolean flag, String fieldName) {
        return Sort.by(Boolean.TRUE.equals(flag) ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName);
    }

    private void checkIfPriceIsCorrect(ProcedureCreateRequest procedureCreateRequest) {
        checkBigDecimalPrices(procedureCreateRequest.billedPrice(), procedureCreateRequest.finalPrice(), procedureCreateRequest.taxRatePercent());
    }

    private void checkIfPriceIsCorrect(ProcedureUpdateRequest procedureUpdateRequest) {
        checkBigDecimalPrices(procedureUpdateRequest.billedPrice(), procedureUpdateRequest.finalPrice(), procedureUpdateRequest.taxRatePercent());
    }

    public void checkBigDecimalPrices(BigDecimal priceBeforeTax, BigDecimal priceAfterTax, BigDecimal taxPercent) {
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal calculatedPriceAfterTax = priceBeforeTax.multiply(taxRateMultiplier).setScale(2, RoundingMode.HALF_UP);
        if (calculatedPriceAfterTax.compareTo(priceAfterTax) != 0) {
            throw new BadRequestException("Prices are not correct!");
        }
    }
}
