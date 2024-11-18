package com.opti_pet.backend_app.rest.request.specification;

import lombok.Builder;

@Builder
public record BaseSpecificationRequest(String inputText, Integer pageNumber, Integer pageSize) {
}
