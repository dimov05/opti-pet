package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "consumable", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class Consumable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available_quantity")
    private Long availableQuantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @Column(name = "tax_rate_percent")
    private BigDecimal taxRatePercent;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}
