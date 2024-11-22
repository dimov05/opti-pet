package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.bill.BillCreateRequest;
import com.opti_pet.backend_app.rest.request.specification.BillSpecificationRequest;
import com.opti_pet.backend_app.rest.response.BillResponse;
import com.opti_pet.backend_app.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class BillController {
    private final BillService billService;

    @GetMapping("/clinic-activity/{clinicId}/bills")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public Page<BillResponse> getAllBillsByClinicIdForManager(@PathVariable(name = "clinicId") String clinicId, BillSpecificationRequest billSpecificationRequest) {
        return billService.getAllBillsByClinicIdForManager(clinicId, billSpecificationRequest);
    }

    @PostMapping("/clinic-activity/{clinicId}/bills")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public BillResponse createBill(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody BillCreateRequest billCreateRequest) {
        return billService.createBill(clinicId, billCreateRequest);
    }
//
//    @DeleteMapping("/clinics/{clinicId}/bills/{billId}")
//    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
//    public BillResponse deleteBillById(@PathVariable("clinicId") String clinicId, @PathVariable("billId") String billId) {
//        return billService.deleteBillById(billId);
//    }
}
