package com.opti_pet.backend_app.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "opti-pet")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "bulstat")
    private String bulstat;

    @Column(name = "name")
    private String name;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "user")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @OneToMany(mappedBy = "user")
    private List<BilledItem> billedItems;

    @OneToMany(mappedBy = "user")
    private List<BilledProcedure> billedProcedures;

    @OneToMany(mappedBy = "owner")
    private List<Patient> patients;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRoleClinic> userRoleClinics;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoleClinics.stream()
                .map(url -> {
                    String role = url.getRole().getName();
                    String clinicId = url.getClinic().getId() != null ? url.getClinic().getId().toString() : "";
                    return new SimpleGrantedAuthority(role + "_" + clinicId);
                })
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
