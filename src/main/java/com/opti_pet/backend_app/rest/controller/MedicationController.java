package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ExcelExportRequest;
import com.opti_pet.backend_app.rest.request.medication.MedicationCreateRequest;
import com.opti_pet.backend_app.rest.request.medication.MedicationSpecificationRequest;
import com.opti_pet.backend_app.rest.request.medication.MedicationUpdateRequest;
import com.opti_pet.backend_app.rest.response.MedicationResponse;
import com.opti_pet.backend_app.service.ExcelExporterService;
import com.opti_pet.backend_app.service.MedicationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;
    private final ExcelExporterService excelExporterService;

    @GetMapping("/clinics/{clinicId}/manager/medications")
    public Page<MedicationResponse> getAllMedicationsByClinicIdForManager(@PathVariable(name = "clinicId") String clinicId, MedicationSpecificationRequest medicationSpecificationRequest) {
        return medicationService.getAllMedicationsByClinicIdForManager(clinicId, medicationSpecificationRequest);
    }
    @GetMapping("/clinics/{clinicId}/medications")
    public List<MedicationResponse> getAllMedicationsByClinicIdState(@PathVariable(name = "clinicId") String clinicId) {
        return medicationService.getAllMedicationsByClinicIdState(clinicId);
    }

    @PostMapping("/clinics/{clinicId}/medications")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public MedicationResponse createMedication(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody MedicationCreateRequest medicationCreateRequest) {
        return medicationService.createMedication(clinicId, medicationCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/medications/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public MedicationResponse updateMedication(@PathVariable("clinicId") String clinicId, @Valid @RequestBody MedicationUpdateRequest medicationUpdateRequest) {
        return medicationService.updateMedication(medicationUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/medications/{medicationId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public MedicationResponse deleteMedicationById(@PathVariable("clinicId") String clinicId, @PathVariable("medicationId") String medicationId) {
        return medicationService.deleteMedicationById(medicationId);
    }

    @PostMapping("/clinics/{clinicId}/medications/export")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void exportMedications(@PathVariable("clinicId") String clinicId, HttpServletResponse response, @RequestBody ExcelExportRequest excelExportRequest) throws IOException {
        excelExporterService.exportMedications(clinicId, response, excelExportRequest);
    }

    @PostMapping("/clinics/{clinicId}/medications/export-template")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void exportExcelMedicationsTemplate(@PathVariable("clinicId") String clinicId, HttpServletResponse response) throws IOException {
        excelExporterService.exportExcelMedicationsTemplate(response);
    }

    @PostMapping(value = "/clinics/{clinicId}/medications/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void importMedications(@PathVariable("clinicId") String clinicId, @RequestPart("file") MultipartFile file) throws IOException {
        excelExporterService.importMedications(clinicId, file);
    }
}
