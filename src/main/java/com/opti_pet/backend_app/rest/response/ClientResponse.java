package com.opti_pet.backend_app.rest.response;

import lombok.Builder;

@Builder
public record ClientResponse(String id, String name, String email, String phoneNumber, String homeAddress,
                             String bulstat, String note) {
}
