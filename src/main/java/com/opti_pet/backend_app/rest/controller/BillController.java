package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.bill.BillCreateRequest;
import com.opti_pet.backend_app.rest.response.BillResponse;
import com.opti_pet.backend_app.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class BillController {
    private final BillService billService;

    @GetMapping("/clinic-activity/{clinicId}/bills")
    @PreAuthorize("@securityService.hasAnyAuthorityInClinic(#clinicId)  || @securityService.hasAdministratorAuthority()")
    public List<BillResponse> getAllBillsByClinicId(@PathVariable(name = "clinicId") String clinicId, @RequestParam(name = "includeAll") Boolean includeAll) {
        return billService.getAllBillsByClinicId(clinicId, includeAll);
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
