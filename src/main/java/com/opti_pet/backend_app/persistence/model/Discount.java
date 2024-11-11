package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "discount", schema = "opti-pet")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "percent_consumables")
    private BigDecimal percentConsumables;

    @Column(name = "percent_procedures")
    private BigDecimal percentProcedures;

    @Column(name = "percent_hospitals")
    private BigDecimal percentHospitals;

    @Column(name = "percent_medications")
    private BigDecimal percentMedications;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

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
