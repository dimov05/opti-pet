package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Patient;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.PatientRepository;
import com.opti_pet.backend_app.rest.request.PatientCreateRequest;
import com.opti_pet.backend_app.rest.request.PatientEditRequest;
import com.opti_pet.backend_app.rest.response.PatientResponse;
import com.opti_pet.backend_app.rest.transformer.PatientTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.DATE_TIME_FORMATTER;
import static com.opti_pet.backend_app.util.AppConstants.PATIENT_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;

    public PatientResponse addNewPatient(PatientCreateRequest patientCreateRequest) {
        User owner = userService.getUserByEmailOrThrowException(patientCreateRequest.ownerEmail());
        Patient patient = PatientTransformer.toEntity(owner, patientCreateRequest);

        return PatientTransformer.toResponse(patientRepository.save(patient));
    }

    public PatientResponse editPatient(UUID patientId, PatientEditRequest patientEditRequest) {
        Patient patient = getPatientByIdOrThrowException(patientId);
        if (patientEditRequest.name() != null && !patientEditRequest.name().equals(patient.getName())) {
            patient.setName(patientEditRequest.name());
        }
        if (patientEditRequest.petType() != null && !patientEditRequest.petType().equals(patient.getPetType())) {
            patient.setPetType(patientEditRequest.petType());
        }
        if (patientEditRequest.ownerEmail() != null && !patientEditRequest.ownerEmail().equals(patient.getOwner().getEmail())) {
            patient.setOwner(userService.getUserByEmailOrThrowException(patientEditRequest.ownerEmail()));
        }
        if (patientEditRequest.birthdate() != null && !patientEditRequest.birthdate().equals(patient.getBirthdate().toString())) {
            patient.setBirthdate(LocalDate.parse(patientEditRequest.birthdate(), DATE_TIME_FORMATTER));
        }
        if (patientEditRequest.microchip() != null && !patientEditRequest.microchip().equals(patient.getMicrochip())) {
            patient.setMicrochip(patientEditRequest.microchip());
        }
        if (patientEditRequest.pendant() != null && !patientEditRequest.pendant().equals(patient.getPendant())) {
            patient.setPendant(patientEditRequest.pendant());
        }
        if (patientEditRequest.passport() != null && !patientEditRequest.passport().equals(patient.getPassport())) {
            patient.setPassport(patientEditRequest.passport());
        }
        if (patientEditRequest.weight() != null && patientEditRequest.weight() != patient.getWeight()) {
            patient.setWeight(patientEditRequest.weight());
        }
        if (patientEditRequest.isDeceased() != null && patientEditRequest.isDeceased() != patient.isDeceased()) {
            patient.setDeceased(patientEditRequest.isDeceased());
        }
        if (patientEditRequest.isNeutered() != null && patientEditRequest.isNeutered() != patient.isNeutered()) {
            patient.setNeutered(patientEditRequest.isNeutered());
        }
        if (patientEditRequest.patientAccessCode() != null && !patientEditRequest.patientAccessCode().equals(patient.getPatientAccessCode())) {
            patient.setPatientAccessCode(patientEditRequest.patientAccessCode());
        }
        if (patientEditRequest.note() != null && !patientEditRequest.note().equals(patient.getNote())) {
            patient.setNote(patientEditRequest.note());
        }

        return PatientTransformer.toResponse(patientRepository.save(patient));
    }

    private Patient getPatientByIdOrThrowException(UUID patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new NotFoundException(PATIENT_ENTITY, UUID_FIELD_NAME, patientId.toString()));
    }
}
