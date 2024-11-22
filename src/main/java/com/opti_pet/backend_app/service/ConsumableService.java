package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Consumable;
import com.opti_pet.backend_app.persistence.repository.ConsumableRepository;
import com.opti_pet.backend_app.rest.request.consumable.ConsumableCreateRequest;
import com.opti_pet.backend_app.rest.request.consumable.ConsumableUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.ExtendedSpecificationRequest;
import com.opti_pet.backend_app.rest.response.ConsumableResponse;
import com.opti_pet.backend_app.rest.transformer.ConsumableTransformer;
import com.opti_pet.backend_app.util.specifications.ConsumableSpecifications;
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
public class ConsumableService {
    private final ConsumableRepository consumableRepository;
    private final ClinicService clinicService;

    @Transactional
    public ConsumableResponse createConsumable(String clinicId, ConsumableCreateRequest consumableCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        checkIfPriceIsCorrect(consumableCreateRequest);
        Consumable consumable = consumableRepository.save(ConsumableTransformer.toEntity(consumableCreateRequest, clinic));

        return ConsumableTransformer.toResponse(consumable);
    }

    @Transactional
    public ConsumableResponse updateConsumable(ConsumableUpdateRequest consumableUpdateRequest) {
        Consumable consumable = getConsumableByIdOrThrowException(UUID.fromString(consumableUpdateRequest.consumableId()));
        checkIfPriceIsCorrect(consumableUpdateRequest);
        updateConsumableField(consumableUpdateRequest::name, consumable::getName, consumable::setName);
        updateConsumableField(consumableUpdateRequest::description, consumable::getDescription, consumable::setDescription);
        if (!consumableUpdateRequest.billedPrice().equals(consumable.getPrice())) {
            consumable.setPrice(consumableUpdateRequest.billedPrice());
        }
        if (!consumableUpdateRequest.finalPrice().equals(consumable.getFinalPrice())) {
            consumable.setFinalPrice(consumableUpdateRequest.finalPrice());
        }
        if (!consumableUpdateRequest.taxRatePercent().equals(consumable.getTaxRatePercent())) {
            consumable.setTaxRatePercent(consumableUpdateRequest.taxRatePercent());
        }
        if (!consumableUpdateRequest.availableQuantity().equals(consumable.getAvailableQuantity())) {
            consumable.setAvailableQuantity(consumableUpdateRequest.availableQuantity());
        }

        consumable.setDateUpdated(LocalDate.now());

        return ConsumableTransformer.toResponse(consumableRepository.save(consumable));
    }

    @Transactional
    public ConsumableResponse deleteConsumableById(String consumableId) {
        Consumable consumable = getConsumableByIdOrThrowException(UUID.fromString(consumableId));
        consumableRepository.delete(consumable);

        return ConsumableTransformer.toResponse(consumable);
    }

    @Transactional
    public List<ConsumableResponse> getAllConsumablesByClinicId(String clinicId) {
        return consumableRepository.findAllByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(ConsumableTransformer::toResponse)
                .toList();
    }

    @Transactional
    public Page<ConsumableResponse> getAllConsumablesByClinicIdForManager(String clinicId, ExtendedSpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return consumableRepository.findAll(getSpecifications(specificationRequest, clinic), pageRequest)
                .map(ConsumableTransformer::toResponse);
    }

    private void updateConsumableField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private Specification<Consumable> getSpecifications(ExtendedSpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<Consumable> specification = ConsumableSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(ConsumableSpecifications.consumableNameOrDescriptionLike(inputText));
        }

        return specification;
    }

    public Consumable getConsumableByIdOrThrowException(UUID consumableId) {
        return consumableRepository.findById(consumableId)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, consumableId.toString()));
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


    private void checkIfPriceIsCorrect(ConsumableCreateRequest consumableCreateRequest) {
        checkBigDecimalPrices(consumableCreateRequest.billedPrice(), consumableCreateRequest.finalPrice(), consumableCreateRequest.taxRatePercent());
    }

    private void checkIfPriceIsCorrect(ConsumableUpdateRequest consumableUpdateRequest) {
        checkBigDecimalPrices(consumableUpdateRequest.billedPrice(), consumableUpdateRequest.finalPrice(), consumableUpdateRequest.taxRatePercent());
    }

    public void checkBigDecimalPrices(BigDecimal priceBeforeTax, BigDecimal priceAfterTax, BigDecimal taxPercent) {
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal calculatedPriceAfterTax = priceBeforeTax.multiply(taxRateMultiplier).setScale(2, RoundingMode.HALF_UP);
        if (calculatedPriceAfterTax.compareTo(priceAfterTax) != 0) {
            throw new BadRequestException("Prices are not correct!");
        }
    }
}
