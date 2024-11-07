package com.opti_pet.backend_app.persistence.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExportTypeEnum {
    ALL("All"),
    SELECTED("Selected");

    private final String exportType;

    ExportTypeEnum(String exportType) {
        this.exportType = exportType;
    }

    @JsonValue
    public String getExportType() {
        return exportType;
    }

    @JsonCreator
    public static ExportTypeEnum fromValue(String value) {
        for (ExportTypeEnum exportTypeEnum : values()) {
            String currentExportType = exportTypeEnum.getExportType();
            if (currentExportType.equalsIgnoreCase(value)) {
                return exportTypeEnum;
            }
        }

        throw new IllegalArgumentException("Invalid value for Export Type Enum: " + value);
    }
}
