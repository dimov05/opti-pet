package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Hospital;
import com.opti_pet.backend_app.persistence.repository.HospitalRepository;
import com.opti_pet.backend_app.rest.request.hospital.HospitalCreateRequest;
import com.opti_pet.backend_app.rest.request.hospital.HospitalUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.SpecificationRequest;
import com.opti_pet.backend_app.rest.response.HospitalBaseResponse;
import com.opti_pet.backend_app.rest.response.HospitalResponse;
import com.opti_pet.backend_app.rest.transformer.HospitalTransformer;
import com.opti_pet.backend_app.util.specifications.HospitalSpecifications;
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
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final ClinicService clinicService;

    @Transactional
    public HospitalResponse createHospital(String clinicId, HospitalCreateRequest hospitalCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        checkIfPriceIsCorrect(hospitalCreateRequest);
        Hospital hospital = hospitalRepository.save(HospitalTransformer.toEntity(hospitalCreateRequest, clinic));

        return HospitalTransformer.toResponse(hospital);
    }

    @Transactional
    public HospitalResponse updateHospital(HospitalUpdateRequest hospitalUpdateRequest) {
        Hospital hospital = getHospitalByIdOrThrowException(UUID.fromString(hospitalUpdateRequest.hospitalId()));
        checkIfPriceIsCorrect(hospitalUpdateRequest);
        updateHospitalField(hospitalUpdateRequest::name, hospital::getName, hospital::setName);
        updateHospitalField(hospitalUpdateRequest::description, hospital::getDescription, hospital::setDescription);
        if (!hospitalUpdateRequest.billedPrice().equals(hospital.getPrice())) {
            hospital.setPrice(hospitalUpdateRequest.billedPrice());
        }
        if (!hospitalUpdateRequest.finalPrice().equals(hospital.getFinalPrice())) {
            hospital.setFinalPrice(hospitalUpdateRequest.finalPrice());
        }
        if (!hospitalUpdateRequest.taxRatePercent().equals(hospital.getTaxRatePercent())) {
            hospital.setTaxRatePercent(hospitalUpdateRequest.taxRatePercent());
        }

        hospital.setDateUpdated(LocalDate.now());

        return HospitalTransformer.toResponse(hospitalRepository.save(hospital));
    }

    @Transactional
    public HospitalResponse deleteHospitalById(String hospitalId) {
        Hospital hospital = getHospitalByIdOrThrowException(UUID.fromString(hospitalId));
        hospitalRepository.delete(hospital);

        return HospitalTransformer.toResponse(hospital);
    }

    @Transactional
    public List<HospitalResponse> getAllHospitalsByClinicId(String clinicId) {
        return hospitalRepository.findAllByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(HospitalTransformer::toResponse)
                .toList();
    }

    @Transactional
    public Page<HospitalResponse> getAllHospitalsByClinicIdForManager(String clinicId, SpecificationRequest specificationRequest) {
        Pageable pageRequest = createPageRequest(specificationRequest);
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));

        return hospitalRepository.findAll(getSpecifications(specificationRequest, clinic), pageRequest)
                .map(HospitalTransformer::toResponse);
    }

    private void updateHospitalField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private Specification<Hospital> getSpecifications(SpecificationRequest specificationRequest, Clinic clinic) {
        UUID clinicId = clinic.getId();
        String inputText = specificationRequest.inputText();
        Specification<Hospital> specification = HospitalSpecifications.clinicIdEquals(clinicId);

        if (inputText != null) {
            specification = specification.and(HospitalSpecifications.hospitalNameOrDescriptionLike(inputText));
        }

        return specification;
    }

    public Hospital getHospitalByIdOrThrowException(UUID hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, hospitalId.toString()));
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


    private void checkIfPriceIsCorrect(HospitalCreateRequest hospitalCreateRequest) {
        checkBigDecimalPrices(hospitalCreateRequest.billedPrice(), hospitalCreateRequest.finalPrice(), hospitalCreateRequest.taxRatePercent());
    }

    private void checkIfPriceIsCorrect(HospitalUpdateRequest hospitalUpdateRequest) {
        checkBigDecimalPrices(hospitalUpdateRequest.billedPrice(), hospitalUpdateRequest.finalPrice(), hospitalUpdateRequest.taxRatePercent());
    }

    public void checkBigDecimalPrices(BigDecimal priceBeforeTax, BigDecimal priceAfterTax, BigDecimal taxPercent) {
        BigDecimal taxRateMultiplier = BigDecimal.ONE.add(taxPercent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        BigDecimal calculatedPriceAfterTax = priceBeforeTax.multiply(taxRateMultiplier).setScale(2, RoundingMode.HALF_UP);
        if (calculatedPriceAfterTax.compareTo(priceAfterTax) != 0) {
            throw new BadRequestException("Prices are not correct!");
        }
    }
}
