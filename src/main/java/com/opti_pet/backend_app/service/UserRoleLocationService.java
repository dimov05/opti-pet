package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleLocation;
import com.opti_pet.backend_app.persistence.repository.UserRoleLocationRepository;
import com.opti_pet.backend_app.rest.transformer.UserRoleLocationTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleLocationService {
    private final UserRoleLocationRepository userRoleLocationRepository;

    public void saveNewUserRoleLocation(User user, Location location, Role role) {
        UserRoleLocation userRoleLocation = UserRoleLocationTransformer.fromData(user, location, role);
        userRoleLocationRepository.save(userRoleLocation);
    }
}
