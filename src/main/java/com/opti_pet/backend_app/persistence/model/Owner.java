package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "owner", schema = "opti-pet")
@Getter
@Setter
@NoArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "bulstat")
    private String bulstat;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private boolean isActive;

}
