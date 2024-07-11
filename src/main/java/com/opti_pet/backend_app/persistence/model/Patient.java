package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patient", schema = "opti-pet")
@Getter
@Setter
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
    private String petType;

    @Column(name = "microchip")
    private String microchip;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "weight")
    private double weight;

    @Column(name = "is_deceased")
    private boolean isDeceased;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "patient")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "patient")
    private List<Note> notes;

    @OneToMany(mappedBy = "patient")
    private List<Bill> bills;
}
