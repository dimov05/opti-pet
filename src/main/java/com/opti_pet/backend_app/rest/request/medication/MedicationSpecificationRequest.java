package com.opti_pet.backend_app.rest.request.medication;

import lombok.Builder;

@Builder
public record MedicationSpecificationRequest(String inputText, Boolean sortByAmount, Boolean sortByAvailableQuantity,
                                             Integer pageNumber, Integer pageSize) {
}