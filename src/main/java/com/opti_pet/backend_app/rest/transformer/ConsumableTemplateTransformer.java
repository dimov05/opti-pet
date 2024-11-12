package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BillTemplate;
import com.opti_pet.backend_app.persistence.model.Consumable;
import com.opti_pet.backend_app.persistence.model.ConsumableTemplate;
import com.opti_pet.backend_app.rest.response.ConsumableTemplateResponse;

public class ConsumableTemplateTransformer {

    public static ConsumableTemplate toEntity(Long quantity, Consumable consumable, BillTemplate billTemplate) {
        return ConsumableTemplate.builder()
                .quantity(quantity)
                .billTemplate(billTemplate)
                .consumable(consumable)
                .build();
    }

    public static ConsumableTemplateResponse toResponse(ConsumableTemplate consumableTemplate) {
        Consumable consumable = consumableTemplate.getConsumable();
        return ConsumableTemplateResponse.builder()
                .consumableId(consumable.getId().toString())
                .name(consumable.getName())
                .description(consumable.getDescription())
                .quantity(consumableTemplate.getQuantity())
                .finalPrice(consumable.getFinalPrice())
                .build();
    }
}
