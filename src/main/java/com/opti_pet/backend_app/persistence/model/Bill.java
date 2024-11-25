package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bill", schema = "opti-pet")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "amount_after_tax")
    private BigDecimal amountAfterTax;

    @Column(name = "amount_before_tax_after_discount")
    private BigDecimal amountBeforeTaxAfterDiscount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "has_invoice")
    private boolean hasInvoice;

    @Column(name = "note")
    private String note;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    private User updatedByUser;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BilledMedication> billedMedications = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BilledConsumable> billedConsumables = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BilledProcedure> billedProcedures = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BookedHospital> bookedHospitals = new ArrayList<>();

    public void addBilledMedication(BilledMedication medication) {
        billedMedications.add(medication);
        medication.setBill(this);
    }

    public void removeBilledMedication(BilledMedication medication) {
        billedMedications.remove(medication);
        medication.setBill(null);
    }

    public void addBilledConsumable(BilledConsumable consumable) {
        billedConsumables.add(consumable);
        consumable.setBill(this);
    }

    public void removeBilledConsumable(BilledConsumable consumable) {
        billedConsumables.remove(consumable);
        consumable.setBill(null);
    }

    public void addBilledProcedure(BilledProcedure procedure) {
        billedProcedures.add(procedure);
        procedure.setBill(this);
    }

    public void removeBilledProcedure(BilledProcedure procedure) {
        billedProcedures.remove(procedure);
        procedure.setBill(null);
    }

    public void addBookedHospital(BookedHospital hospital) {
        bookedHospitals.add(hospital);
        hospital.setBill(this);
    }

    public void removeBookedHospital(BookedHospital hospital) {
        bookedHospitals.remove(hospital);
        hospital.setBill(null);
    }
}
