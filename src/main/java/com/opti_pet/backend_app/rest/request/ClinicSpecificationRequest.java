package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

@Builder
public record ClinicSpecificationRequest(String inputText, Integer pageNumber, Integer pageSize) {
}