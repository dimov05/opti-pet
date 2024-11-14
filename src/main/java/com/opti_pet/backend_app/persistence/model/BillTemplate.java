package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public void addConsumableTemplate(ConsumableTemplate consumableTemplate) {
        if (consumableTemplates == null) {
            consumableTemplates = new ArrayList<>();
        }
        consumableTemplates.add(consumableTemplate);
    }

    public void addMedicationTemplate(MedicationTemplate medicationTemplate) {
        if (medicationTemplates == null) {
            medicationTemplates = new ArrayList<>();
        }
        medicationTemplates.add(medicationTemplate);
    }

    public void addProcedureTemplate(ProcedureTemplate procedureTemplate) {
        if (procedureTemplates == null) {
            procedureTemplates = new ArrayList<>();
        }
        procedureTemplates.add(procedureTemplate);
    }

    public void removeConsumableTemplate(ConsumableTemplate consumableTemplate) {
        if (consumableTemplates != null) {
            consumableTemplates.remove(consumableTemplate);
        }
    }

    public void removeMedicationTemplate(MedicationTemplate medicationTemplate) {
        if (medicationTemplates != null) {
            medicationTemplates.remove(medicationTemplate);
        }
    }

    public void removeProcedureTemplate(ProcedureTemplate procedureTemplate) {
        if (procedureTemplates != null) {
            procedureTemplates.remove(procedureTemplate);
        }
    }
}
