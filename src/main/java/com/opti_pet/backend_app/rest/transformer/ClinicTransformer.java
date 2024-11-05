package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import com.opti_pet.backend_app.rest.request.ClinicCreateRequest;
import com.opti_pet.backend_app.rest.response.ClinicBaseResponse;
import com.opti_pet.backend_app.rest.response.ClinicResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ClinicTransformer {
    public static Clinic toEntity(ClinicCreateRequest clinicCreateRequest, User owner) {
        return Clinic.builder()
                .name(clinicCreateRequest.name())
                .email(clinicCreateRequest.email())
                .city(clinicCreateRequest.city())
                .address(clinicCreateRequest.address())
                .phoneNumber(clinicCreateRequest.phoneNumber())
                .clinicRestrictionsEnabled(clinicCreateRequest.clinicRestrictionsEnabled())
                .latitude(clinicCreateRequest.latitude())
                .longitude(clinicCreateRequest.longitude())
                .isActive(true)
                .owner(owner)
                .vaccinations(new ArrayList<>())
                .discounts(new ArrayList<>())
                .notes(new ArrayList<>())
                .bills(new ArrayList<>())
                .medications(new ArrayList<>())
                .procedures(new ArrayList<>())
                .billedMedications(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .patients(new ArrayList<>())
                .userRoleClinics(new ArrayList<>())
                .build();
    }

    public static ClinicResponse toResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId().toString())
                .name(clinic.getName())
                .email(clinic.getEmail())
                .ownerEmail(clinic.getOwner().getEmail())
                .city(clinic.getCity())
                .address(clinic.getAddress())
                .phoneNumber(clinic.getPhoneNumber())
                .clinicRestrictionsEnabled(clinic.getClinicRestrictionsEnabled())
                .latitude(clinic.getLatitude())
                .longitude(clinic.getLongitude())
                .employeesCount(clinic.getUserRoleClinics().stream()
                        .map(UserRoleClinic::getUserId)
                        .collect(Collectors.toSet()).size())
                .isActive(clinic.isActive())
                .build();
    }

    public static ClinicBaseResponse toBaseResponse(Clinic clinic) {
        return ClinicBaseResponse.builder()
                .id(clinic.getId().toString())
                .name(clinic.getName())
                .build();
    }
}
