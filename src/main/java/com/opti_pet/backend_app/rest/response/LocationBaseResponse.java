package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record LocationBaseResponse(String id, String name, boolean restrictionByGps) {
}
