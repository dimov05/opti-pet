package com.opti_pet.backend_app.rest.request.billTemplate;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import java.util.List;

@Builder
public record BillTemplateCreateRequest(@NotBlank String name, @Length(max = 255) String description,
                                        List<ConsumableTemplateRequest> consumableTemplates,
                                        List<MedicationTemplateRequest> medicationTemplates,
                                        List<ProcedureTemplateRequest> procedureTemplates) {
}
