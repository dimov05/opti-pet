package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

@Builder
public record ProcedureSpecificationRequest(String inputText,Boolean sortByAmount, Integer pageNumber, Integer pageSize) {
}