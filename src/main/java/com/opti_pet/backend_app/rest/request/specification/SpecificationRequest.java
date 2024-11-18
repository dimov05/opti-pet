package com.opti_pet.backend_app.rest.request.specification;

import lombok.Builder;

@Builder
public record SpecificationRequest(String inputText,Boolean sortByAmount, Integer pageNumber, Integer pageSize) {
}
