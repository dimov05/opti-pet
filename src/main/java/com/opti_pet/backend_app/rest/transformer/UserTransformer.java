package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.LocationCreateUserRequest;
import com.opti_pet.backend_app.rest.request.UserRegisterRequest;
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
                .isActive(true)
                .notes(new ArrayList<>())
                .billedItems(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .build();
    }

    public static User toEntity(LocationCreateUserRequest locationCreateUserRequest, String encodedPassword) {
        return User.builder()
                .email(locationCreateUserRequest.userEmail())
                .name(locationCreateUserRequest.userName())
                .password(encodedPassword)
                .phoneNumber(locationCreateUserRequest.userPhoneNumber())
                .jobTitle(locationCreateUserRequest.userJobTitle())
                .isActive(true)
                .notes(new ArrayList<>())
                .billedItems(new ArrayList<>())
                .patients(new ArrayList<>())
                .vaccinations(new ArrayList<>())
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
                .locations(UserRoleLocationTransformer.toLocationRoleResponse(user.getUserRoleLocations()))
                .isActive(user.isActive())
                .build();
    }

}
