package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.ClinicRepository;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.response.ClinicExtendedResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;
import com.opti_pet.backend_app.rest.transformer.ClinicTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.opti_pet.backend_app.util.AppConstants.CLINIC_ENTITY;
import static com.opti_pet.backend_app.util.AppConstants.UUID_FIELD_NAME;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final UserService userService;
    private final ClinicRepository clinicRepository;

    @Transactional
    public ClinicResponse createClinic(ClinicCreateRequest clinicCreateRequest) {
        User owner = userService.getUserByEmailOrThrowException(clinicCreateRequest.ownerEmail());
        checkIfClinicAlreadyExists(clinicCreateRequest);
        Clinic clinic = ClinicTransformer.toEntity(clinicCreateRequest, owner);

        return ClinicTransformer.toResponse(clinicRepository.save(clinic));
    }

    private void checkIfClinicAlreadyExists(ClinicCreateRequest clinicCreateRequest) {
        if (clinicRepository.existsByName(clinicCreateRequest.name())) {
            throw new BadRequestException("Clinic with this name already exists");
        }
    }

    public Clinic getClinicByIdOrThrowException(String id) {
        return clinicRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, id));
    }

    public ClinicExtendedResponse getClinicByIdOrThrowException(UUID id) {
        return ClinicTransformer.toExtendedResponse(clinicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, id.toString())));
    }
}
