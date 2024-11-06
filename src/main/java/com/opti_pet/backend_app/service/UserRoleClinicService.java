package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.persistence.repository.UserRoleClinicRepository;
import com.opti_pet.backend_app.rest.transformer.UserRoleClinicTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.opti_pet.backend_app.util.AppConstants.EMAIL_FIELD_NAME;
import static com.opti_pet.backend_app.util.AppConstants.USER_ENTITY;

@Service
@RequiredArgsConstructor
public class UserRoleClinicService {
    private final UserRoleClinicRepository userRoleClinicRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public List<UserRoleClinic> saveNewUserRoleClinic(User user, Clinic clinic, Role role) {
        UserRoleClinic userRoleClinic = UserRoleClinicTransformer.fromData(user, clinic, role);
        List<UserRoleClinic> userRoleClinics = new ArrayList<>();
        userRoleClinics.add(userRoleClinicRepository.save(userRoleClinic));
        return userRoleClinics;
    }

    @Transactional
    public void createRoleForOwnerOfClinic(Clinic clinic, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new NotFoundException(USER_ENTITY, EMAIL_FIELD_NAME, ownerEmail));
        List<Role> rolesToSet = roleRepository.findAllById(List.of(2L, 3L, 4L, 5L));

        rolesToSet.forEach(role ->
                userRoleClinicRepository.save(UserRoleClinicTransformer.fromData(owner, clinic, role)));
    }

    @Transactional
    public void deleteUserRoleClinicByUserAndClinic(User user, Clinic clinic) {
        userRoleClinicRepository.deleteByUserIdAndClinicId(user.getId(), clinic.getId());
    }

    public List<UserRoleClinic> setRolesForUserAndClinic(User user, List<Role> roles, Clinic clinic) {
        userRoleClinicRepository.deleteByUserIdAndClinicId(user.getId(), clinic.getId());
        List<UserRoleClinic> userRoleClinicsToAdd = new ArrayList<>();
        roles.forEach(role -> userRoleClinicsToAdd.add(
                userRoleClinicRepository.save(UserRoleClinicTransformer.fromData(user, clinic, role))));
        return userRoleClinicsToAdd;
    }
}
