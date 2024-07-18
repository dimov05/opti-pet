package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.repository.LocationRepository;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.rest.request.UserRegisterRequest;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.rest.transformer.UserTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.opti_pet.backend_app.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRoleLocationService userRoleLocationService;
    private final LocationRepository locationRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserByEmailOrThrowException(email);
    }

    @Transactional
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        User user = UserTransformer.toEntity(userRegisterRequest, passwordEncoder.encode(userRegisterRequest.password()));

        user = userRepository.save(user);

        Location location = locationRepository.findById(LOCATION_UUID)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, LOCATION_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);

        userRoleLocationService.saveNewUserRoleLocation(user, location, role);


        return UserTransformer.toResponse(user);
    }

    public User getUserByEmailOrThrowException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_ENTITY, EMAIL_FIELD_NAME, email));
    }
}
