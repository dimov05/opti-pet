package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.enums.PetType;
import com.opti_pet.backend_app.persistence.model.Patient;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.PatientRepository;
import com.opti_pet.backend_app.rest.request.patient.PatientCreateRequest;
import com.opti_pet.backend_app.rest.request.patient.PatientEditRequest;
import com.opti_pet.backend_app.rest.response.PatientResponse;
import com.opti_pet.backend_app.rest.transformer.PatientTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;

    @Transactional
    public PatientResponse addNewPatient(PatientCreateRequest patientCreateRequest) {
        User owner = userService.getUserByPhoneNumberOrThrowException(patientCreateRequest.ownerPhoneNumber());
        Patient patient = PatientTransformer.toEntity(owner, patientCreateRequest);

        return PatientTransformer.toResponse(patientRepository.save(patient));
    }

    @Transactional
    public PatientResponse editPatient(UUID patientId, PatientEditRequest patientEditRequest) {
        Patient patient = getPatientByIdOrThrowException(patientId);

        if (patientEditRequest.petType() != null && !patientEditRequest.petType().equals(patient.getPetType().getBreed())) {
            patient.setPetType(PetType.fromValue(patientEditRequest.petType()));
        }
        if (patientEditRequest.ownerEmail() != null && !patientEditRequest.ownerEmail().equals(patient.getOwner().getEmail())) {
            patient.setOwner(userService.getUserByEmailOrThrowException(patientEditRequest.ownerEmail()));
        }
        if (patientEditRequest.birthdate() != null && !patientEditRequest.birthdate().equals(patient.getBirthdate().toString())) {
            patient.setBirthdate(LocalDate.parse(patientEditRequest.birthdate(), DATE_FORMATTER));
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
        updatePatientField(patientEditRequest::name, patient::getName, patient::setName);
        updatePatientField(patientEditRequest::microchip, patient::getMicrochip, patient::setMicrochip);
        updatePatientField(patientEditRequest::pendant, patient::getPendant, patient::setPendant);
        updatePatientField(patientEditRequest::passport, patient::getPassport, patient::setPassport);
        updatePatientField(patientEditRequest::patientAccessCode, patient::getPatientAccessCode, patient::setPatientAccessCode);
        updatePatientField(patientEditRequest::note, patient::getNote, patient::setNote);

        return PatientTransformer.toResponse(patientRepository.save(patient));
    }

    private void updatePatientField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private Patient getPatientByIdOrThrowException(UUID patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new NotFoundException(PATIENT_ENTITY, UUID_FIELD_NAME, patientId.toString()));
    }
}
