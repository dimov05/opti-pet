package com.opti_pet.backend_app.persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetType {
    DOG_BOLOGNESE("Dog - Bolognese"),
    DOG_MALINOA("Dog - Malinoa"),
    DOG_GOLDEN_RETRIEVER("Dog - Golden Retriever"),
    CAT_SIAMESE("Cat - Siamese");

    private final String petType;

    PetType(String petType) {
        this.petType = petType;
    }

    @JsonValue
    public String getPetType() {
        return petType;
    }

    @JsonCreator
    public static PetType fromValue(String value) {
        for (PetType petTypeEnum : values()) {
            String currentPetType = petTypeEnum.getPetType();
            if (currentPetType.equalsIgnoreCase(value)) {
                return petTypeEnum;
            }
        }

        throw new IllegalArgumentException("Invalid value for Pet Type Enum: " + value);
    }
}
