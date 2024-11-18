package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.billTemplate.BillTemplateCreateRequest;
import com.opti_pet.backend_app.rest.request.billTemplate.BillTemplateUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.SpecificationRequest;
import com.opti_pet.backend_app.rest.response.BillTemplateResponse;
import com.opti_pet.backend_app.service.BillTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillTemplateController {
    private final BillTemplateService billTemplateService;

    @GetMapping("/clinics/{clinicId}/bill-templates")
    public List<BillTemplateResponse> getAllBillTemplatesByClinicId(@PathVariable(name = "clinicId") String clinicId) {
        return billTemplateService.getAllBillTemplatesByClinicIdState(clinicId);
    }

    @GetMapping("/clinics/{clinicId}/manager/bill-templates")
    public Page<BillTemplateResponse> getAllBillTemplatesByClinicIdForManager(@PathVariable(name = "clinicId") String clinicId, SpecificationRequest specificationRequest) {
        return billTemplateService.getAllBillTemplatesByClinicIdForManager(clinicId, specificationRequest);
    }

    @PostMapping("/clinics/{clinicId}/bill-templates")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public BillTemplateResponse createBillTemplate(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody BillTemplateCreateRequest billTemplateCreateRequest) {
        return billTemplateService.createBillTemplate(clinicId, billTemplateCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/billTemplates/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public BillTemplateResponse updateBillTemplate(@PathVariable("clinicId") String clinicId, @Valid @RequestBody BillTemplateUpdateRequest billTemplateUpdateRequest) {
        return billTemplateService.updateBillTemplate(billTemplateUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/billTemplates/{billTemplateId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public BillTemplateResponse deleteBillTemplateById(@PathVariable("clinicId") String clinicId, @PathVariable("billTemplateId") String billTemplateId) {
        return billTemplateService.deleteBillTemplateById(billTemplateId);
    }
}
