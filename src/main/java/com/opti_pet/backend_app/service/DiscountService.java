package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Discount;
import com.opti_pet.backend_app.persistence.repository.DiscountRepository;
import com.opti_pet.backend_app.rest.request.discount.DiscountCreateRequest;
import com.opti_pet.backend_app.rest.request.discount.DiscountUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.BaseSpecificationRequest;
import com.opti_pet.backend_app.rest.response.DiscountResponse;
import com.opti_pet.backend_app.rest.transformer.DiscountTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.CLINIC_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final ClinicService clinicService;

    @Transactional
    public DiscountResponse createDiscount(String clinicId, DiscountCreateRequest discountCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        Discount discount = discountRepository.save(DiscountTransformer.toEntity(discountCreateRequest, clinic));

        return DiscountTransformer.toResponse(discount);
    }

    @Transactional
    public DiscountResponse updateDiscount(DiscountUpdateRequest discountUpdateRequest) {
        Discount discount = getDiscountByIdOrThrowException(discountUpdateRequest.discountId());
        updateDiscountField(discountUpdateRequest::name, discount::getName, discount::setName);

        if (!discountUpdateRequest.percentConsumables().equals(discount.getPercentConsumables())) {
            discount.setPercentConsumables(discountUpdateRequest.percentConsumables());
        }
        if (!discountUpdateRequest.percentHospitals().equals(discount.getPercentHospitals())) {
            discount.setPercentHospitals(discountUpdateRequest.percentHospitals());
        }
        if (!discountUpdateRequest.percentMedications().equals(discount.getPercentMedications())) {
            discount.setPercentMedications(discountUpdateRequest.percentMedications());
        }
        if (!discountUpdateRequest.percentProcedures().equals(discount.getPercentProcedures())) {
            discount.setPercentProcedures(discountUpdateRequest.percentProcedures());
        }

        discount.setDateUpdated(LocalDate.now());

        return DiscountTransformer.toResponse(discountRepository.save(discount));
    }

    @Transactional
    public DiscountResponse deleteDiscountById(Long discountId) {
        Discount discount = getDiscountByIdOrThrowException(discountId);
        discountRepository.delete(discount);

        return DiscountTransformer.toResponse(discount);
    }

    @Transactional
    public List<DiscountResponse> getAllDiscountsByClinicId(String clinicId) {
        return discountRepository.findAllByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(DiscountTransformer::toResponse)
                .toList();
    }

    @Transactional
    public Page<DiscountResponse> getAllDiscountsByClinicIdForManager(String clinicId, BaseSpecificationRequest specificationRequest) {
        return null;
    }

    private void updateDiscountField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }
    private Discount getDiscountByIdOrThrowException(Long discountId) {
        return discountRepository.findById(discountId)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, discountId.toString()));
    }
}
