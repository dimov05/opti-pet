package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clinic", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_phone_number")
    private String ownerPhoneNumber;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "clinic")
    private List<Location> locations;
}
