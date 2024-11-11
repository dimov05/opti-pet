package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record HospitalBaseResponse(String id, String name, String description) {
}
