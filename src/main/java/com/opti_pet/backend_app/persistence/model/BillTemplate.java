package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bill_template", schema = "opti-pet")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BillTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_updated")
    private LocalDate dateUpdated;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "bill_template_consumable", schema = "opti-pet",
            joinColumns = @JoinColumn(name = "bill_template_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "consumable_id", referencedColumnName = "id"))
    private List<Consumable> consumables;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "bill_template_medication", schema = "opti-pet",
            joinColumns = @JoinColumn(name = "bill_template_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id", referencedColumnName = "id"))
    private List<Medication> medications;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "bill_template_procedure", schema = "opti-pet",
            joinColumns = @JoinColumn(name = "bill_template_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id", referencedColumnName = "id"))
    private List<Procedure> procedures;
}
