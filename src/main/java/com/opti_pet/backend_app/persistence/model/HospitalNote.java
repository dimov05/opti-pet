package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hospital_note", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class HospitalNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @Column(name = "is_public")
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "booked_hospital_id")
    private BookedHospital bookedHospital;
}
