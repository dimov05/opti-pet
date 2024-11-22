package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "booked_hospital", schema = "opti-pet")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookedHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "billed_price")
    private BigDecimal billedPrice;

    @Column(name = "tax_rate_percent")
    private BigDecimal taxRatePercent;

    @Column(name = "booked_hours")
    private Long bookedHours;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "billed_date")
    private LocalDateTime billedDate;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "bookedHospital")
    private List<HospitalNote> notes;
}
