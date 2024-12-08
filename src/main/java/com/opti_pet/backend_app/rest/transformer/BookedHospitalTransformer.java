package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BookedHospitalResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.opti_pet.backend_app.util.AppConstants.DATE_TIME_FORMATTER;

public class BookedHospitalTransformer {

    public static BookedHospitalResponse toResponse(BookedHospital bookedHospital) {
        return BookedHospitalResponse.builder()
                .id(bookedHospital.getId().toString())
                .name(bookedHospital.getName())
                .billedPrice(bookedHospital.getBilledPrice())
                .taxRatePercent(bookedHospital.getTaxRatePercent())
                .bookedHours(bookedHospital.getBookedHours())
                .startDate(bookedHospital.getStartDate().format(DATE_TIME_FORMATTER))
                .endDate(bookedHospital.getEndDate().format(DATE_TIME_FORMATTER))
                .billedDate(bookedHospital.getBilledDate().format(DATE_TIME_FORMATTER))
                .discountPercent(bookedHospital.getBill().getDiscount() != null
                        ? bookedHospital.getBill().getDiscount().getPercentMedications()
                        : BigDecimal.ZERO)
                .billingUserName(bookedHospital.getUser().getName())
                .build();
    }

    public static BookedHospital toEntity(Hospital hospital, User employee, Clinic clinic, Patient patient, Bill bill) {
        return BookedHospital.builder()
                .name(hospital.getName())
                .description(hospital.getDescription())
                .billedPrice(hospital.getPrice())
                .bookedHours(0L)
                .startDate(LocalDateTime.now())
                .endDate(null)
                .taxRatePercent(hospital.getTaxRatePercent())
                .billedDate(LocalDateTime.now())
                .bill(bill)
                .user(employee)
                .clinic(clinic)
                .patient(patient)
                .notes(new ArrayList<>())
                .build();
    }
}
