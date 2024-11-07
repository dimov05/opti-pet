package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ExcelExportRequest(String exportType, List<UUID> uuids) {
}
