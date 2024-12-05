package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.*;
import com.opti_pet.backend_app.rest.response.BilledConsumableResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.opti_pet.backend_app.util.AppConstants.DATE_FORMATTER;
import static com.opti_pet.backend_app.util.AppConstants.DATE_TIME_FORMATTER;

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
                .billedDate(billedConsumable.getBilledDate().format(DATE_FORMATTER))
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
