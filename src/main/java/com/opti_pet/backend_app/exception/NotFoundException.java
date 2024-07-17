package com.opti_pet.backend_app.exception;

import static com.opti_pet.backend_app.util.ErrorConstants.ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entityClass, String fieldName, String value) {
        super(String.format(ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, entityClass, fieldName, value));
    }

    public NotFoundException(String message) {
        super(message);
    }
}
