package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bill", schema = "opti-pet")
@Getter
@Setter
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

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "has_invoice")
    private boolean hasInvoice;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToMany(mappedBy = "bill")
    private List<BilledMedication> billedMedications;

    @OneToMany(mappedBy = "bill")
    private List<BilledConsumable> billedConsumables;

    @OneToMany(mappedBy = "bill")
    private List<BilledProcedure> billedProcedures;

}
