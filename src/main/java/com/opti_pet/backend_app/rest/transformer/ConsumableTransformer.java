package com.opti_pet.backend_app.rest.transformer;

import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Consumable;
import com.opti_pet.backend_app.rest.request.consumable.ConsumableCreateRequest;
import com.opti_pet.backend_app.rest.response.ConsumableResponse;

import java.time.LocalDate;

import static com.opti_pet.backend_app.util.AppConstants.DATE_FORMATTER;

public class ConsumableTransformer {

    public static ConsumableResponse toResponse(Consumable consumable) {
        return ConsumableResponse.builder()
                .id(consumable.getId().toString())
                .name(consumable.getName())
                .description(consumable.getDescription())
                .price(consumable.getPrice())
                .availableQuantity(consumable.getAvailableQuantity())
                .finalPrice(consumable.getFinalPrice())
                .taxRatePercent(consumable.getTaxRatePercent())
                .dateAdded(consumable.getDateAdded().format(DATE_FORMATTER))
                .dateUpdated(consumable.getDateUpdated().format(DATE_FORMATTER))
                .clinic(ClinicTransformer.toBaseResponse(consumable.getClinic()))
                .build();
    }

    public static Consumable toEntity(ConsumableCreateRequest consumableCreateRequest, Clinic clinic) {
        return Consumable.builder()
                .name(consumableCreateRequest.name())
                .description(consumableCreateRequest.description())
                .price(consumableCreateRequest.billedPrice())
                .availableQuantity(consumableCreateRequest.availableQuantity())
                .finalPrice(consumableCreateRequest.finalPrice())
                .taxRatePercent(consumableCreateRequest.taxRatePercent())
                .dateAdded(LocalDate.now())
                .dateUpdated(LocalDate.now())
                .isActive(true)
                .clinic(clinic)
                .build();
    }
}
