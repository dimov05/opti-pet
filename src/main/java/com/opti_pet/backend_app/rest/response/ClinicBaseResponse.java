package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record ClinicBaseResponse(String id, String name, boolean restrictionByGps) {
}
