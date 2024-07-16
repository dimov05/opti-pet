package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

@Builder
public record UserLoginRequest(String email, String password) {
}
