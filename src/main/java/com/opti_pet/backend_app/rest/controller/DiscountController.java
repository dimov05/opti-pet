package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.discount.DiscountCreateRequest;
import com.opti_pet.backend_app.rest.request.discount.DiscountUpdateRequest;
import com.opti_pet.backend_app.rest.request.specification.BaseSpecificationRequest;
import com.opti_pet.backend_app.rest.response.DiscountResponse;
import com.opti_pet.backend_app.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/clinics/{clinicId}/discounts")
    public List<DiscountResponse> getAllDiscountsByClinicId(@PathVariable(name = "clinicId") String clinicId) {
        return discountService.getAllDiscountsByClinicId(clinicId);
    }

    @GetMapping("/clinics/{clinicId}/manager/discounts")
    public Page<DiscountResponse> getAllDiscountsByClinicIdForManager(@PathVariable(name = "clinicId") String clinicId, BaseSpecificationRequest specificationRequest) {
        return discountService.getAllDiscountsByClinicIdForManager(clinicId, specificationRequest);
    }

    @PostMapping("/clinics/{clinicId}/discounts")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public DiscountResponse createDiscount(@PathVariable(name = "clinicId") String clinicId, @Valid @RequestBody DiscountCreateRequest discountCreateRequest) {
        return discountService.createDiscount(clinicId, discountCreateRequest);
    }

    @PutMapping("/clinics/{clinicId}/discounts/edit")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public DiscountResponse updateDiscount(@PathVariable("clinicId") String clinicId, @Valid @RequestBody DiscountUpdateRequest discountUpdateRequest) {
        return discountService.updateDiscount(discountUpdateRequest);
    }

    @DeleteMapping("/clinics/{clinicId}/discounts/{discountId}")
    @PreAuthorize("@securityService.hasAuthority('CLINIC_MANAGER_' + #clinicId) || @securityService.hasAdministratorAuthority()")
    public DiscountResponse deleteDiscountById(@PathVariable("clinicId") String clinicId, @PathVariable("discountId") Long discountId) {
        return discountService.deleteDiscountById(discountId);
    }
}
