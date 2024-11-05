package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clinic", schema = "opti-pet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "clinic_restrictions_enabled")
    private Boolean clinicRestrictionsEnabled;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "clinic")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "clinic")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "clinic")
    private List<Note> notes;

    @OneToMany(mappedBy = "clinic")
    private List<Bill> bills;

    @OneToMany(mappedBy = "clinic")
    private List<Medication> medications;

    @OneToMany(mappedBy = "clinic")
    private List<Consumable> consumables;

    @OneToMany(mappedBy = "clinic")
    private List<Procedure> procedures;

    @OneToMany(mappedBy = "clinic")
    private List<BilledMedication> billedMedications;

    @OneToMany(mappedBy = "clinic")
    private List<BilledConsumable> billedConsumables;

    @OneToMany(mappedBy = "clinic")
    private List<BilledProcedure> billedProcedures;

    @ManyToMany(mappedBy = "clinics")
    private List<Patient> patients;

    @OneToMany(mappedBy = "clinic")
    private List<UserRoleClinic> userRoleClinics;
}
