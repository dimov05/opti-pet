package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Patient;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.patient.PatientCreateRequest;
import com.opti_pet.backend_app.rest.response.PatientBaseResponse;
import com.opti_pet.backend_app.rest.response.PatientResponse;

import java.time.LocalDate;
import java.util.Objects;

import static com.opti_pet.backend_app.util.AppConstants.DATE_FORMATTER;

public class PatientTransformer {

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .petType(patient.getPetType().getBreed())
                .birthdate(patient.getBirthdate().format(DATE_FORMATTER))
                .microchip(patient.getMicrochip())
                .pendant(patient.getPendant())
                .passport(patient.getPassport())
                .weight(patient.getWeight())
                .isDeceased(patient.isDeceased())
                .isNeutered(patient.isNeutered())
                .note(patient.getNote())
                .owner(UserTransformer.toResponse(patient.getOwner()))
                .build();
    }

    public static Patient toEntity(User owner, PatientCreateRequest patientCreateRequest) {
        return Patient.builder()
                .name(patientCreateRequest.name())
                .petType(patientCreateRequest.petType())
                .microchip(patientCreateRequest.microchip())
                .pendant(patientCreateRequest.pendant())
                .passport(patientCreateRequest.passport())
                .birthdate(LocalDate.parse(patientCreateRequest.birthdate(), DATE_FORMATTER))
                .weight(patientCreateRequest.weight())
                .isDeceased(Objects.equals(patientCreateRequest.isDeceased(),true))
                .isNeutered(Objects.equals(patientCreateRequest.isNeutered(),true))
                .note(patientCreateRequest.note())
                .patientAccessCode("0000")
                .owner(owner)
                .build();
    }

    public static PatientBaseResponse toBaseResponse(Patient patient) {
        return PatientBaseResponse.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .petType(patient.getPetType().getBreed())
                .birthdate(patient.getBirthdate().format(DATE_FORMATTER))
                .build();
    }
}