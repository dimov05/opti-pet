package com.opti_pet.backend_app.exception;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorModel(int statusCode, List<String> messages) {
}
