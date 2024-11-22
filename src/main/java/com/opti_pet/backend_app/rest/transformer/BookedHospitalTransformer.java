package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BookedHospitalResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookedHospitalTransformer {

    public static BookedHospitalResponse toResponse(BookedHospital bookedHospital) {
        return BookedHospitalResponse.builder()
                .id(bookedHospital.getId().toString())
                .name(bookedHospital.getName())
                .billedPrice(bookedHospital.getBilledPrice())
                .taxRatePercent(bookedHospital.getTaxRatePercent())
                .bookedHours(bookedHospital.getBookedHours())
                .startDate(bookedHospital.getStartDate().toString())
                .endDate(bookedHospital.getEndDate().toString())
                .billedDate(bookedHospital.getBilledDate().toString())
                .discountPercent(bookedHospital.getBill().getDiscount().getPercentHospitals())
                .billingUserName(bookedHospital.getUser().getName())
                .build();
    }

    public static BookedHospital toEntity(Hospital hospital, User employee, Clinic clinic, Patient patient) {
        return BookedHospital.builder()
                .name(hospital.getName())
                .description(hospital.getDescription())
                .billedPrice(hospital.getPrice())
                .bookedHours(0L)
                .startDate(LocalDateTime.now())
                .endDate(null)
                .taxRatePercent(hospital.getTaxRatePercent())
                .billedDate(LocalDateTime.now())
                .bill(null)
                .user(employee)
                .clinic(clinic)
                .patient(patient)
                .notes(new ArrayList<>())
                .build();
    }
}
