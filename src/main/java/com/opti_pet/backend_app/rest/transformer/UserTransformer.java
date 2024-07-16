package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.request.UserRegisterRequest;
import com.opti_pet.backend_app.rest.response.UserResponse;

import java.util.ArrayList;

public class UserTransformer {
    public static User fromRegisterRequest(UserRegisterRequest userRegisterRequest, String encodedPassword) {
        return User.builder()
                .email(userRegisterRequest.email())
                .isActive(true)
                .name(userRegisterRequest.name())
                .notes(new ArrayList<>())
                .billedItems(new ArrayList<>())
                .password(encodedPassword)
                .patients(new ArrayList<>())
                .jobTitle(userRegisterRequest.jobTitle())
                .vaccinations(new ArrayList<>())
                .billedProcedures(new ArrayList<>())
                .phoneNumber(userRegisterRequest.phoneNumber())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .build();
    }
}
