package com.opti_pet.backend_app.rest.request.specification;

import lombok.Builder;

@Builder
public record ExtendedSpecificationRequest(String inputText, Boolean sortByAmount, Boolean sortByAvailableQuantity,
                                           Integer pageNumber, Integer pageSize) {
}
