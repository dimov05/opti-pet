package com.opti_pet.backend_app.rest.request.user;

import lombok.Builder;

@Builder
public record UserLoginRequest(String email, String password) {
}
