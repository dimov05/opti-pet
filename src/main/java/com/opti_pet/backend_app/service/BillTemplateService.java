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
import java.util.Objects;
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

        List<ConsumableTemplateRequest> consumables = billTemplateCreateRequest.consumableTemplates();
        List<MedicationTemplateRequest> medications = billTemplateCreateRequest.medicationTemplates();
        List<ProcedureTemplateRequest> procedures = billTemplateCreateRequest.procedureTemplates();
        setTemplateConsumables(consumables, billTemplate);
        setTemplateMedications(medications, billTemplate);
        setTemplateProcedures(procedures, billTemplate);

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
        billTemplate.setConsumableTemplates(syncConsumableTemplates(billTemplate, billTemplateUpdateRequest.consumableTemplates()));
        billTemplate.setMedicationTemplates(syncMedicationTemplates(billTemplate, billTemplateUpdateRequest.medicationTemplates()));
        billTemplate.setProcedureTemplates(syncProcedureTemplates(billTemplate, billTemplateUpdateRequest.procedureTemplates()));

        billTemplate.setDateUpdated(LocalDate.now());
        return BillTemplateTransformer.toResponse(billTemplateRepository.save(billTemplate));
    }

    @Transactional
    public BillTemplateResponse deleteBillTemplateById(String billTemplateId) {
        BillTemplate billTemplate = getBillTemplateByIdOrThrowException(UUID.fromString(billTemplateId));
        billTemplateRepository.delete(billTemplate);

        return BillTemplateTransformer.toResponse(billTemplate);
    }

    private List<ConsumableTemplate> syncConsumableTemplates(BillTemplate billTemplate, List<ConsumableTemplateRequest> consumableTemplateRequests) {
        List<ConsumableTemplate> currentConsumableTemplates = billTemplate.getConsumableTemplates();
        List<UUID> consumableRequests = consumableTemplateRequests.stream()
                .map(consumableRequest -> {
                    String id = consumableRequest.id();
                    return UUID.fromString(id);
                })
                .toList();

        consumableTemplateRequests.forEach(request -> {
            UUID requestConsumableId = UUID.fromString(request.id());
            Long requestedQuantity = request.quantity();
            ConsumableTemplate currentConsumableTemplate = currentConsumableTemplates.stream()
                    .filter(current -> current.getConsumable().getId().equals(requestConsumableId))
                    .findFirst()
                    .orElse(null);
            if (currentConsumableTemplate != null) {
                if (!Objects.equals(currentConsumableTemplate.getQuantity(), requestedQuantity)) {
                    currentConsumableTemplate.setQuantity(requestedQuantity);
                }
            } else {
                Consumable consumable = consumableRepository.findById(requestConsumableId)
                        .orElseThrow(() -> new BadRequestException(String.format(
                                ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, CONSUMABLE_ENTITY, ID_FIELD_NAME, requestConsumableId)));
                currentConsumableTemplates.add(ConsumableTemplateTransformer.toEntity(requestedQuantity, consumable, billTemplate));
            }
        });

        List<ConsumableTemplate> consumableTemplatesToSet = currentConsumableTemplates.stream()
                .filter(currentConsumableTemplate -> consumableRequests.contains(currentConsumableTemplate.getConsumable().getId()))
                .toList();
        billTemplate.setConsumableTemplates(consumableTemplatesToSet);

        return consumableTemplatesToSet;
    }

    private List<MedicationTemplate> syncMedicationTemplates(BillTemplate billTemplate, List<MedicationTemplateRequest> medicationTemplateRequests) {
        List<MedicationTemplate> currentMedicationTemplates = billTemplate.getMedicationTemplates();
        List<UUID> medicationRequests = medicationTemplateRequests.stream()
                .map(medicationRequest -> {
                    String id = medicationRequest.id();
                    return UUID.fromString(id);
                })
                .toList();

        medicationTemplateRequests.forEach(request -> {
            UUID requestMedicationId = UUID.fromString(request.id());
            Long requestedQuantity = request.quantity();
            MedicationTemplate currentMedicationTemplate = currentMedicationTemplates.stream()
                    .filter(current -> current.getMedication().getId().equals(requestMedicationId))
                    .findFirst()
                    .orElse(null);
            if (currentMedicationTemplate != null) {
                if (!Objects.equals(currentMedicationTemplate.getQuantity(), requestedQuantity)) {
                    currentMedicationTemplate.setQuantity(requestedQuantity);
                }
            } else {
                Medication medication = medicationRepository.findById(requestMedicationId)
                        .orElseThrow(() -> new BadRequestException(String.format(
                                ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, MEDICATION_ENTITY, ID_FIELD_NAME, requestMedicationId)));
                currentMedicationTemplates.add(MedicationTemplateTransformer.toEntity(requestedQuantity, medication, billTemplate));
            }
        });

        List<MedicationTemplate> medicationTemplatesToSet = currentMedicationTemplates.stream()
                .filter(currentMedicationTemplate -> medicationRequests.contains(currentMedicationTemplate.getMedication().getId()))
                .toList();
        billTemplate.setMedicationTemplates(medicationTemplatesToSet);

        return medicationTemplatesToSet;
    }

    private List<ProcedureTemplate> syncProcedureTemplates(BillTemplate billTemplate, List<ProcedureTemplateRequest> procedureTemplateRequests) {
        List<ProcedureTemplate> currentProcedureTemplates = billTemplate.getProcedureTemplates();
        List<UUID> procedureRequests = procedureTemplateRequests.stream()
                .map(procedureRequest -> {
                    String id = procedureRequest.id();
                    return UUID.fromString(id);
                })
                .toList();

        procedureTemplateRequests.forEach(request -> {
            UUID requestProcedureId = UUID.fromString(request.id());
            Long requestedQuantity = request.quantity();
            ProcedureTemplate currentProcedureTemplate = currentProcedureTemplates.stream()
                    .filter(current -> current.getProcedure().getId().equals(requestProcedureId))
                    .findFirst()
                    .orElse(null);
            if (currentProcedureTemplate != null) {
                if (!Objects.equals(currentProcedureTemplate.getQuantity(), requestedQuantity)) {
                    currentProcedureTemplate.setQuantity(requestedQuantity);
                }
            } else {
                Procedure procedure = procedureRepository.findById(requestProcedureId)
                        .orElseThrow(() -> new BadRequestException(String.format(
                                ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, PROCEDURE_ENTITY, ID_FIELD_NAME, requestProcedureId)));
                currentProcedureTemplates.add(ProcedureTemplateTransformer.toEntity(requestedQuantity, procedure, billTemplate));
            }
        });

        List<ProcedureTemplate> procedureTemplatesToSet = currentProcedureTemplates.stream()
                .filter(currentProcedureTemplate -> procedureRequests.contains(currentProcedureTemplate.getProcedure().getId()))
                .toList();
        billTemplate.setProcedureTemplates(procedureTemplatesToSet);

        return procedureTemplatesToSet;
    }

    private void updateBillTemaplateField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private void setTemplateProcedures(List<ProcedureTemplateRequest> requests, BillTemplate billTemplate) {
        requests.forEach(procedure -> {
            Procedure procedureToAdd = procedureRepository.findById(UUID.fromString(procedure.id()))
                    .orElseThrow(() -> new BadRequestException(String.format(
                            ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, PROCEDURE_ENTITY, ID_FIELD_NAME, procedure.id())));
            billTemplate.addProcedureTemplate(procedureTemplateRepository.saveAndFlush(ProcedureTemplateTransformer.toEntity(procedure.quantity(), procedureToAdd, billTemplate)));
        });
    }


    private void setTemplateMedications(List<MedicationTemplateRequest> requests, BillTemplate billTemplate) {
        requests.forEach(medication -> {
            Medication medicationToAdd = medicationRepository.findById(UUID.fromString(medication.id()))
                    .orElseThrow(() -> new BadRequestException(String.format(
                            ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, MEDICATION_ENTITY, ID_FIELD_NAME, medication.id())));
            billTemplate.addMedicationTemplate(medicationTemplateRepository.saveAndFlush(MedicationTemplateTransformer.toEntity(medication.quantity(), medicationToAdd, billTemplate)));
        });
    }

    private void setTemplateConsumables(List<ConsumableTemplateRequest> requests, BillTemplate billTemplate) {
        requests.forEach(consumable -> {
            Consumable consumableToAdd = consumableRepository.findById(UUID.fromString(consumable.id()))
                    .orElseThrow(() -> new BadRequestException(String.format(
                            ENTITY_NOT_FOUND_BY_FIELD_AND_VALUE_EXCEPTION_TEMPLATE, CONSUMABLE_ENTITY, ID_FIELD_NAME, consumable.id())));
            billTemplate.addConsumableTemplate(consumableTemplateRepository.saveAndFlush(
                    ConsumableTemplateTransformer.toEntity(consumable.quantity(), consumableToAdd, billTemplate)));
        });
    }

    private BillTemplate getBillTemplateByIdOrThrowException(UUID billTemplateId) {
        return billTemplateRepository.findById(billTemplateId)
                .orElseThrow(() -> new NotFoundException(BILL_TEMPLATE_ENTITY, UUID_FIELD_NAME, billTemplateId.toString()));
    }
}
