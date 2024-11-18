package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.persistence.repository.*;
import com.opti_pet.backend_app.rest.request.billTemplate.*;
import com.opti_pet.backend_app.rest.response.BillTemplateResponse;
import com.opti_pet.backend_app.rest.transformer.BillTemplateTransformer;
import com.opti_pet.backend_app.rest.transformer.ConsumableTemplateTransformer;
import com.opti_pet.backend_app.rest.transformer.MedicationTemplateTransformer;
import com.opti_pet.backend_app.rest.transformer.ProcedureTemplateTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.*;
import static com.opti_pet.backend_app.util.ErrorConstants.ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE;

@Service
@RequiredArgsConstructor
public class BillTemplateService {
    private final BillTemplateRepository billTemplateRepository;
    private final ClinicService clinicService;
    private final UserService userService;
    private final JwtService jwtService;
    private final ConsumableRepository consumableRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final ConsumableTemplateRepository consumableTemplateRepository;
    private final MedicationTemplateRepository medicationTemplateRepository;
    private final ProcedureTemplateRepository procedureTemplateRepository;

    @Transactional
    public List<BillTemplateResponse> getAllBillTemplatesByClinicIdForManager(String clinicId) {
        return billTemplateRepository.findBillTemplateByClinic_Id(UUID.fromString(clinicId))
                .stream()
                .map(BillTemplateTransformer::toResponse)
                .toList();
    }

    @Transactional
    public BillTemplateResponse createBillTemplate(String clinicId, BillTemplateCreateRequest billTemplateCreateRequest) {
        Clinic clinic = clinicService.getClinicByIdOrThrowException(UUID.fromString(clinicId));
        User authenticatedEmployee = userService.getUserByEmailOrThrowException(jwtService.extractEmailFromToken());
        BillTemplate billTemplate = BillTemplateTransformer.toEntity(billTemplateCreateRequest, clinic, authenticatedEmployee);
        billTemplateRepository.save(billTemplate);

        setConsumableTemplates(billTemplate, billTemplateCreateRequest.consumableTemplates());
        setMedicationTemplates(billTemplate, billTemplateCreateRequest.medicationTemplates());
        setProcedureTemplates(billTemplate, billTemplateCreateRequest.procedureTemplates());

        return BillTemplateTransformer.toResponse(billTemplateRepository.save(billTemplate));
    }

    @Transactional
    public BillTemplateResponse updateBillTemplate(BillTemplateUpdateRequest billTemplateUpdateRequest) {
        BillTemplate billTemplate = getBillTemplateByIdOrThrowException(UUID.fromString(billTemplateUpdateRequest.id()));
        User authenticatedEmployee = userService.getUserByEmailOrThrowException(jwtService.extractEmailFromToken());

        updateBillTemaplateField(billTemplateUpdateRequest::name, billTemplate::getName, billTemplate::setName);
        updateBillTemaplateField(billTemplateUpdateRequest::description, billTemplate::getDescription, billTemplate::setDescription);
        if (authenticatedEmployee.getId() != billTemplate.getUser().getId()) {
            billTemplate.setUser(authenticatedEmployee);
        }
        setConsumableTemplates(billTemplate, billTemplateUpdateRequest.consumableTemplates());
        setMedicationTemplates(billTemplate, billTemplateUpdateRequest.medicationTemplates());
        setProcedureTemplates(billTemplate, billTemplateUpdateRequest.procedureTemplates());

        billTemplate.setDateUpdated(LocalDate.now());
        return BillTemplateTransformer.toResponse(billTemplateRepository.save(billTemplate));
    }

    @Transactional
    public BillTemplateResponse deleteBillTemplateById(String billTemplateId) {
        BillTemplate billTemplate = getBillTemplateByIdOrThrowException(UUID.fromString(billTemplateId));
        billTemplateRepository.delete(billTemplate);

        return BillTemplateTransformer.toResponse(billTemplate);
    }

    private void setConsumableTemplates(BillTemplate billTemplate, List<ConsumableTemplateRequest> consumableTemplateRequests) {
        List<ConsumableTemplate> consumableTemplatesToSet = consumableTemplateRequests
                .stream()
                .map(request -> {
                    Long requestedQuantity = request.quantity();
                    UUID requestConsumableId = UUID.fromString(request.id());
                    Consumable consumable = consumableRepository.findById(requestConsumableId)
                            .orElseThrow(() -> new BadRequestException(String.format(
                                    ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, CONSUMABLE_ENTITY, ID_FIELD_NAME, requestConsumableId)));
                    return consumableTemplateRepository.save(ConsumableTemplateTransformer.toEntity(requestedQuantity, consumable, billTemplate));
                })
                .toList();
        billTemplate.setConsumableTemplates(consumableTemplatesToSet);
    }

    private void setMedicationTemplates(BillTemplate billTemplate, List<MedicationTemplateRequest> medicationTemplateRequests) {
        List<MedicationTemplate> medicationTemplatesToSet = medicationTemplateRequests
                .stream()
                .map(request -> {
                    Long requestedQuantity = request.quantity();
                    UUID requestMedicationId = UUID.fromString(request.id());
                    Medication medication = medicationRepository.findById(requestMedicationId)
                            .orElseThrow(() -> new BadRequestException(String.format(
                                    ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, MEDICATION_ENTITY, ID_FIELD_NAME, requestMedicationId)));
                    return medicationTemplateRepository.save(MedicationTemplateTransformer.toEntity(requestedQuantity, medication, billTemplate));
                })
                .toList();
        billTemplate.setMedicationTemplates(medicationTemplatesToSet);
    }

    private void setProcedureTemplates(BillTemplate billTemplate, List<ProcedureTemplateRequest> procedureTemplateRequests) {
        List<ProcedureTemplate> procedureTemplatesToSet = procedureTemplateRequests
                .stream()
                .map(request -> {
                    Long requestedQuantity = request.quantity();
                    UUID requestProcedureId = UUID.fromString(request.id());
                    Procedure procedure = procedureRepository.findById(requestProcedureId)
                            .orElseThrow(() -> new BadRequestException(String.format(
                                    ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, PROCEDURE_ENTITY, ID_FIELD_NAME, requestProcedureId)));
                    return procedureTemplateRepository.save(ProcedureTemplateTransformer.toEntity(requestedQuantity, procedure, billTemplate));
                })
                .toList();
        billTemplate.setProcedureTemplates(procedureTemplatesToSet);
    }

    private void updateBillTemaplateField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private BillTemplate getBillTemplateByIdOrThrowException(UUID billTemplateId) {
        return billTemplateRepository.findById(billTemplateId)
                .orElseThrow(() -> new NotFoundException(BILL_TEMPLATE_ENTITY, UUID_FIELD_NAME, billTemplateId.toString()));
    }
}
