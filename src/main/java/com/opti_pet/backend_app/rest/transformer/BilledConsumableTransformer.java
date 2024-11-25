package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BilledConsumableResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BilledConsumableTransformer {

    public static BilledConsumableResponse toResponse(BilledConsumable billedConsumable) {
        return BilledConsumableResponse.builder()
                .id(billedConsumable.getId().toString())
                .name(billedConsumable.getName())
                .billedPrice(billedConsumable.getBilledPrice())
                .taxRatePercent(billedConsumable.getTaxRatePercent())
                .quantity(billedConsumable.getQuantity())
                .discountPercent(billedConsumable.getBill().getDiscount() != null
                        ? billedConsumable.getBill().getDiscount().getPercentMedications()
                        : BigDecimal.ZERO)
                .billedDate(billedConsumable.getBilledDate().toString())
                .billingUserName(billedConsumable.getUser().getName())
                .build();
    }

    public static BilledConsumable toEntity(Long quantity, Consumable consumable, User employee, Clinic clinic, Bill bill) {
        return BilledConsumable.builder()
                .name(consumable.getName())
                .description(consumable.getDescription())
                .billedPrice(consumable.getPrice())
                .taxRatePercent(consumable.getTaxRatePercent())
                .quantity(quantity)
                .billedDate(LocalDateTime.now())
                .bill(bill)
                .user(employee)
                .clinic(clinic)
                .build();
    }
}
