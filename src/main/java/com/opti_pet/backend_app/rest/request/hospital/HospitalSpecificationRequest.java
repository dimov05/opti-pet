package com.opti_pet.backend_app.rest.request.hospital;

import lombok.Builder;

@Builder
public record HospitalSpecificationRequest(String inputText, Boolean sortByAmount, Integer pageNumber, Integer pageSize) {
}