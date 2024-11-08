package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.ExcelExportRequest;
import com.opti_pet.backend_app.rest.request.ProcedureCreateRequest;
import com.opti_pet.backend_app.rest.request.ProcedureSpecificationRequest;
import com.opti_pet.backend_app.rest.request.ProcedureUpdateRequest;
import com.opti_pet.backend_app.rest.response.ProcedureResponse;
import com.opti_pet.backend_app.service.ExcelExporterService;
import com.opti_pet.backend_app.service.ProcedureService;
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
public class ProcedureController {
    private final ProcedureService procedureService;
    private final ExcelExporterService excelExporterService;

    @GetMapping("/clinics/{clinicId}/procedures")
    public Page<ProcedureResponse> getAllProceduresByClinicId(@PathVariable(name = "clinicId") String clinicId, ProcedureSpecificationRequest procedureSpecificationRequest) {
        return procedureService.getAllProceduresByClinicId(clinicId, procedureSpecificationRequest);
    }

    @PostMapping("/clinics/{clinicId}/procedures")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ProcedureResponse createProcedure(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody ProcedureCreateRequest procedureCreateRequest) {
        return procedureService.createProcedure(clinicId, procedureCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/procedures/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ProcedureResponse updateProcedure(@PathVariable("clinicId") String clinicId, @Valid @RequestBody ProcedureUpdateRequest procedureUpdateRequest) {
        return procedureService.updateProcedure(procedureUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/procedures/{procedureId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public ProcedureResponse deleteProcedureById(@PathVariable("clinicId") String clinicId, @PathVariable("procedureId") String procedureId) {
        return procedureService.deleteProcedureById(procedureId);
    }

    @PostMapping("/clinics/{clinicId}/procedures/export")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void exportProcedures(@PathVariable("clinicId") String clinicId, HttpServletResponse response, @RequestBody ExcelExportRequest excelExportRequest) throws IOException {
        excelExporterService.exportProcedures(clinicId, response, excelExportRequest);
    }
    @PostMapping("/clinics/{clinicId}/procedures/export-template")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void exportExcelProceduresTemplate(@PathVariable("clinicId") String clinicId, HttpServletResponse response) throws IOException {
        excelExporterService.exportExcelProceduresTemplate(response);
    }

    @PostMapping(value = "/clinics/{clinicId}/procedures/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public void importProcedures(@PathVariable("clinicId") String clinicId, @RequestPart("file") MultipartFile file) throws IOException {
        excelExporterService.importProcedures(clinicId, file);
    }
}
