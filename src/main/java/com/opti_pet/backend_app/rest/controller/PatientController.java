package com.opti_pet.backend_app.rest.controller;

import com.opti_pet.backend_app.rest.request.PatientCreateRequest;
import com.opti_pet.backend_app.rest.request.PatientEditRequest;
import com.opti_pet.backend_app.rest.response.PatientResponse;
import com.opti_pet.backend_app.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/new-patient")
    public PatientResponse addNewPatient(@Valid @RequestBody PatientCreateRequest patientCreateRequest) {
        return patientService.addNewPatient(patientCreateRequest);
    }

    @PutMapping("/{patientId}/edit")
    public PatientResponse editPatient(@PathVariable("patientId") UUID patientId, PatientEditRequest patientEditRequest) {
        return patientService.editPatient(patientId, patientEditRequest);
    }
}
