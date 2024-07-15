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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "location", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class Location {
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

    @Column(name = "location_restrictions_enabled")
    private Boolean locationRestrictionsEnabled;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToMany(mappedBy = "location")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "location")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "location")
    private List<Note> notes;

    @OneToMany(mappedBy = "location")
    private List<Bill> bills;

    @OneToMany(mappedBy = "location")
    private List<Item> items;

    @OneToMany(mappedBy = "location")
    private List<Procedure> procedures;

    @OneToMany(mappedBy = "location")
    private List<BilledItem> billedItems;

    @OneToMany(mappedBy = "location")
    private List<BilledProcedure> billedProcedures;

    @ManyToMany(mappedBy = "locations")
    private List<Patient> patients;

    @OneToMany(mappedBy = "location")
    private List<UserRoleLocation> userRoleLocations;
}
