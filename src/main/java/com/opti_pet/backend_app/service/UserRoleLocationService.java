package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleLocation;
import com.opti_pet.backend_app.persistence.repository.RoleRepository;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.persistence.repository.UserRoleLocationRepository;
import com.opti_pet.backend_app.rest.transformer.UserRoleLocationTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.opti_pet.backend_app.util.AppConstants.EMAIL_FIELD_NAME;
import static com.opti_pet.backend_app.util.AppConstants.USER_ENTITY;

@Service
@RequiredArgsConstructor
public class UserRoleLocationService {
    private final UserRoleLocationRepository userRoleLocationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void saveNewUserRoleLocation(User user, Location location, Role role) {
        UserRoleLocation userRoleLocation = UserRoleLocationTransformer.fromData(user, location, role);
        userRoleLocationRepository.save(userRoleLocation);
    }

    @Transactional
    public void createRoleForOwnerOfLocation(Location location, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new NotFoundException(USER_ENTITY, EMAIL_FIELD_NAME, ownerEmail));
        List<Role> rolesToSet = roleRepository.findAllById(List.of(2L, 3L, 4L, 5L));

        rolesToSet.forEach(role ->
                userRoleLocationRepository.save(UserRoleLocationTransformer.fromData(owner, location, role)));
    }

    @Transactional
    public void deleteUserRoleLocation(User user, Location location, Role role) {
        userRoleLocationRepository
                .findByUserIdAndRoleIdAndLocationId(user.getId(), role.getId(), location.getId())
                .ifPresent(userRoleLocationRepository::delete);
    }

    @Transactional
    public void deleteUserRoleLocationByUserAndLocation(User user, Location location) {
        userRoleLocationRepository.deleteByUserIdAndLocationId(user.getId(), location.getId());
    }
}
