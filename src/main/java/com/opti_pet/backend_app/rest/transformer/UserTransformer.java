package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.clinic.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.user.ClinicActivityCreateClientRequest;
import com.opti_pet.backend_app.rest.request.user.UserRegisterAsAdminRequest;
import com.opti_pet.backend_app.rest.request.user.UserRegisterRequest;
import com.opti_pet.backend_app.rest.response.ClientExtendedResponse;
import com.opti_pet.backend_app.rest.response.ClientResponse;
import com.opti_pet.backend_app.rest.response.UserResponse;

import java.util.ArrayList;

public class UserTransformer {
    public static User toEntity(UserRegisterRequest userRegisterRequest, String encodedPassword) {
        return User.builder()
                .email(userRegisterRequest.email())
                .name(userRegisterRequest.name())
                .password(encodedPassword)
                .phoneNumber(userRegisterRequest.phoneNumber())
                .jobTitle(userRegisterRequest.jobTitle())
                .homeAddress(userRegisterRequest.homeAddress())
                .bulstat(userRegisterRequest.bulstat())
                .note(null)
                .isActive(true)
                .notes(new ArrayList<>())
                .billedMedications(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .build();
    }

    public static User toEntity(ClinicActivityCreateClientRequest clinicActivityCreateClientRequest, String encodedPassword) {
        return User.builder()
                .email(clinicActivityCreateClientRequest.email())
                .name(clinicActivityCreateClientRequest.name())
                .password(encodedPassword)
                .phoneNumber(clinicActivityCreateClientRequest.phoneNumber())
                .jobTitle("Client")
                .homeAddress(clinicActivityCreateClientRequest.homeAddress())
                .bulstat(clinicActivityCreateClientRequest.bulstat())
                .note(clinicActivityCreateClientRequest.note())
                .isActive(true)
                .notes(new ArrayList<>())
                .billedMedications(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .build();
    }

    public static User toEntity(ClinicCreateUserRequest clinicCreateUserRequest, String encodedPassword) {
        return User.builder()
                .email(clinicCreateUserRequest.email())
                .name(clinicCreateUserRequest.userName())
                .password(encodedPassword)
                .phoneNumber(clinicCreateUserRequest.userPhoneNumber())
                .homeAddress(clinicCreateUserRequest.homeAddress())
                .bulstat(clinicCreateUserRequest.bulstat())
                .jobTitle(clinicCreateUserRequest.userJobTitle())
                .note(null)
                .isActive(true)
                .notes(new ArrayList<>())
                .billedMedications(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .build();
    }

    public static User toEntity(UserRegisterAsAdminRequest userRegisterAsAdminRequest, String encodedPassword) {
        return User.builder()
                .email(userRegisterAsAdminRequest.email())
                .name(userRegisterAsAdminRequest.name())
                .password(encodedPassword)
                .phoneNumber(userRegisterAsAdminRequest.phoneNumber())
                .jobTitle(userRegisterAsAdminRequest.jobTitle())
                .homeAddress(userRegisterAsAdminRequest.homeAddress())
                .bulstat(null)
                .note(null)
                .isActive(true)
                .notes(new ArrayList<>())
                .billedMedications(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
                .userRoleClinics(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .homeAddress(user.getHomeAddress())
                .bulstat(user.getBulstat())
                .jobTitle(user.getJobTitle())
                .note(user.getNote())
                .clinics(UserRoleClinicTransformer.toClinicRoleResponse(user.getUserRoleClinics()))
                .isActive(user.isActive())
                .isAdministrator(user.getUserRoleClinics().stream().anyMatch(userRoleClinic -> userRoleClinic.getRole().getName().startsWith("ADMINISTRATOR")))
                .build();
    }

    public static ClientResponse toClientResponse(User user) {
        return ClientResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .homeAddress(user.getHomeAddress())
                .bulstat(user.getBulstat())
                .note(user.getNote())
                .build();
    }

    public static ClientExtendedResponse toClientExtendedResponse(User user) {
        return ClientExtendedResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .homeAddress(user.getHomeAddress())
                .bulstat(user.getBulstat())
                .note(user.getNote())
                .patients(user.getPatients().stream().map(PatientTransformer::toResponse).toList())
                .build();
    }
}
