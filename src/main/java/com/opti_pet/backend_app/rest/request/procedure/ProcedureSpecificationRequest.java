package com.opti_pet.backend_app.rest.request.procedure;

import lombok.Builder;

@Builder
public record ProcedureSpecificationRequest(String inputText,Boolean sortByAmount, Integer pageNumber, Integer pageSize) {
}