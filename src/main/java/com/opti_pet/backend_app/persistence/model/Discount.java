package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "discount", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "percent_items")
    private BigDecimal percentItems;

    @Column(name = "percent_procedures")
    private BigDecimal percentProcedures;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToMany(mappedBy = "discount")
    private List<BilledMedication> billedMedications;

    @OneToMany(mappedBy = "discount")
    private List<BilledConsumable> billedConsumables;

    @OneToMany(mappedBy = "discount")
    private List<BilledProcedure> billedProcedures;

    @OneToMany(mappedBy = "discount")
    private List<BookedHospital> bookedHospitals;
}
