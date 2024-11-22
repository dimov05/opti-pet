package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.BilledConsumable;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Consumable;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.rest.response.BilledConsumableResponse;

import java.time.LocalDateTime;

public class BilledConsumableTransformer {

    public static BilledConsumableResponse toResponse(BilledConsumable billedConsumable) {
        return BilledConsumableResponse.builder()
                .id(billedConsumable.getId().toString())
                .name(billedConsumable.getName())
                .billedPrice(billedConsumable.getBilledPrice())
                .taxRatePercent(billedConsumable.getTaxRatePercent())
                .quantity(billedConsumable.getQuantity())
                .discountPercent(billedConsumable.getBill().getDiscount().getPercentConsumables())
                .billedDate(billedConsumable.getBilledDate().toString())
                .billingUserName(billedConsumable.getUser().getName())
                .build();
    }

    public static BilledConsumable toEntity(Long quantity, Consumable consumable, User employee, Clinic clinic) {
        return BilledConsumable.builder()
                .name(consumable.getName())
                .description(consumable.getDescription())
                .billedPrice(consumable.getPrice())
                .taxRatePercent(consumable.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(null)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
