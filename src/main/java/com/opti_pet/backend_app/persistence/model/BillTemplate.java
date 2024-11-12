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

    @OneToMany(mappedBy = "billTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumableTemplate> consumableTemplates;

    @OneToMany(mappedBy = "billTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationTemplate> medicationTemplates;

    @OneToMany(mappedBy = "billTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcedureTemplate> procedureTemplates;
}
