package com.opti_pet.backend_app.persistence.model;

import com.opti_pet.backend_app.persistence.enums.PetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patient", schema = "opti-pet")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type")
    private PetType petType;

    @Column(name = "microchip")
    private String microchip;

    @Column(name = "pendant")
    private String pendant;

    @Column(name = "passport")
    private String passport;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "weight")
    private double weight;

    @Column(name = "is_deceased")
    private boolean isDeceased;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    @Column(name = "note")
    private String note;

    @Column(name = "patient_access_code")
    private String patientAccessCode;

    @OneToMany(mappedBy = "patient")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "patient")
    private List<Note> notes;

    @OneToMany(mappedBy = "patient")
    private List<Bill> bills;

    @OneToMany(mappedBy = "patient")
    private List<BookedHospital> bookedHospitals;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "patient_clinic", schema = "opti-pet",
            joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "clinic_id", referencedColumnName = "id"))
    private List<Clinic> clinics;
}
