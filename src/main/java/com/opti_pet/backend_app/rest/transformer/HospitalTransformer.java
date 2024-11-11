package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Hospital;
import com.opti_pet.backend_app.rest.request.hospital.HospitalCreateRequest;
import com.opti_pet.backend_app.rest.response.HospitalBaseResponse;
import com.opti_pet.backend_app.rest.response.HospitalResponse;

import java.time.LocalDate;

public class HospitalTransformer {

    public static HospitalResponse toResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId().toString())
                .name(hospital.getName())
                .description(hospital.getDescription())
                .price(hospital.getPrice())
                .finalPrice(hospital.getFinalPrice())
                .taxRatePercent(hospital.getTaxRatePercent())
                .dateAdded(hospital.getDateAdded().toString())
                .dateUpdated(hospital.getDateUpdated().toString())
                .clinic(ClinicTransformer.toBaseResponse(hospital.getClinic()))
                .build();
    }

    public static HospitalBaseResponse toBaseResponse(Hospital hospital) {
        return HospitalBaseResponse.builder()
                .id(hospital.getId().toString())
                .name(hospital.getName())
                .description(hospital.getDescription())
                .build();
    }

    public static Hospital toEntity(HospitalCreateRequest hospitalCreateRequest, Clinic clinic) {
        return Hospital.builder()
                .name(hospitalCreateRequest.name())
                .description(hospitalCreateRequest.description())
                .price(hospitalCreateRequest.billedPrice())
                .finalPrice(hospitalCreateRequest.finalPrice())
                .taxRatePercent(hospitalCreateRequest.taxRatePercent())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
