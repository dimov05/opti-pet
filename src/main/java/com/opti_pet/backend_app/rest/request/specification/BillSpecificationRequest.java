package com.opti_pet.backend_app.rest.request.specification;

import lombok.Builder;

@Builder
public record BillSpecificationRequest(String inputText, Boolean sortByRemainingAmount, Boolean sortByAmountAfterTax,
                                       Integer pageNumber, Integer pageSize) {
}
