package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ExcelExportRequest;
import com.opti_pet.backend_app.rest.request.ConsumableCreateRequest;
import com.opti_pet.backend_app.rest.request.ConsumableSpecificationRequest;
import com.opti_pet.backend_app.rest.request.ConsumableUpdateRequest;
import com.opti_pet.backend_app.rest.response.ConsumableResponse;
import com.opti_pet.backend_app.service.ExcelExporterService;
import com.opti_pet.backend_app.service.ConsumableService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsumableController {
    private final ConsumableService consumableService;
    private final ExcelExporterService excelExporterService;

    @GetMapping("/clinics/{clinicId}/consumables")
    public Page<ConsumableResponse> getAllConsumablesByClinicId(@PathVariable(name = "clinicId") String clinicId, ConsumableSpecificationRequest consumableSpecificationRequest) {
        return consumableService.getAllConsumablesByClinicId(clinicId, consumableSpecificationRequest);
    }

    @PostMapping("/clinics/{clinicId}/consumables")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ConsumableResponse createConsumable(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody ConsumableCreateRequest consumableCreateRequest) {
        return consumableService.createConsumable(clinicId, consumableCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/consumables/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ConsumableResponse updateConsumable(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ConsumableUpdateRequest consumableUpdateRequest) {
        return consumableService.updateConsumable(consumableUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/consumables/{consumableId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ConsumableResponse deleteConsumableById(@PathVariable("clinicId") String clinicId, @PathVariable("consumableId") String consumableId) {
        return consumableService.deleteConsumableById(consumableId);
    }

    @PostMapping("/clinics/{clinicId}/consumables/export")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void exportToExcel(@PathVariable("clinicId") String clinicId, HttpServletResponse response, @RequestBody ExcelExportRequest excelExportRequest) throws IOException {
        excelExporterService.exportConsumables(clinicId, response, excelExportRequest);
    }

    @PostMapping("/clinics/{clinicId}/consumables/export-template")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void downloadExcelTemplate(@PathVariable("clinicId") String clinicId, HttpServletResponse response) throws IOException {
        excelExporterService.exportExcelConsumablesTemplate(response);
    }

    @PostMapping(value = "/clinics/{clinicId}/consumables/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void importFromExcel(@PathVariable("clinicId") String clinicId, @RequestPart("file") MultipartFile file) throws IOException {
        excelExporterService.importConsumables(clinicId, file);
    }
}
