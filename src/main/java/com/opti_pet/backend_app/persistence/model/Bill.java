package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "bill")
    private List<BilledMedication> billedMedications;

    @OneToMany(mappedBy = "bill")
    private List<BilledConsumable> billedConsumables;

    @OneToMany(mappedBy = "bill")
    private List<BilledProcedure> billedProcedures;

    @OneToMany(mappedBy = "bill")
    private List<BookedHospital> bookedHospitals;
}
