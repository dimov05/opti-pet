package com.opti_pet.backend_app.rest.request.user;

import lombok.Builder;

@Builder
public record UserSpecificationRequest(String inputText, Integer pageNumber, Integer pageSize) {
}
