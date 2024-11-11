package com.opti_pet.backend_app.rest.request.consumable;

import lombok.Builder;

@Builder
public record ConsumableSpecificationRequest(String inputText, Boolean sortByAmount, Boolean sortByAvailableQuantity,
                                             Integer pageNumber, Integer pageSize) {
}