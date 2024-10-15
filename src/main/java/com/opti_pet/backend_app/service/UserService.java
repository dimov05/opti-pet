package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Location;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleLocation;
import com.opti_pet.backend_app.persistence.repository.LocationRepository;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.rest.request.LocationCreateUserRequest;
import com.opti_pet.backend_app.rest.request.UserChangePasswordRequest;
import com.opti_pet.backend_app.rest.request.UserEditProfileRequest;
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

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.opti_pet.backend_app.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRoleLocationService userRoleLocationService;
    private final LocationRepository locationRepository;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserByEmailOrThrowException(email);
    }

    @Transactional
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.password().equals(userRegisterRequest.confirmPassword())) {
            throw new BadRequestException("Password and confirm password does not match!");
        }
        User user = UserTransformer.toEntity(userRegisterRequest, passwordEncoder.encode(userRegisterRequest.password()));

        user = userRepository.save(user);

        Location location = locationRepository.findById(DEFAULT_LOCATION_UUID)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, DEFAULT_LOCATION_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);

        List<UserRoleLocation> userRoleLocations = userRoleLocationService.saveNewUserRoleLocation(user, location, role);
        user.setUserRoleLocations(userRoleLocations);

        return UserTransformer.toResponse(userRepository.save(user));
    }

    @Transactional
    public User registerUserAsManager(LocationCreateUserRequest locationCreateUserRequest) {
        if (!locationCreateUserRequest.userPassword().equals(locationCreateUserRequest.userConfirmPassword())) {
            throw new BadRequestException("Password and confirm password does not match!");
        }
        User user = UserTransformer.toEntity(locationCreateUserRequest, passwordEncoder.encode(locationCreateUserRequest.userPassword()));
        user = userRepository.save(user);

        Location location = locationRepository.findById(DEFAULT_LOCATION_UUID)
                .orElseThrow(() -> new NotFoundException(LOCATION_ENTITY, UUID_FIELD_NAME, DEFAULT_LOCATION_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);
        List<UserRoleLocation> userRoleLocations = userRoleLocationService.saveNewUserRoleLocation(user, location, role);
        user.setUserRoleLocations(userRoleLocations);

        return userRepository.save(user);
    }

    @Transactional
    public UserResponse editProfile(String userId, UserEditProfileRequest userEditProfileRequest) {
        User user = getUserByIdOrThrowException(userId);

        updateUserField(userEditProfileRequest::name, user::getName, user::setName);
        updateUserField(userEditProfileRequest::homeAddress, user::getHomeAddress, user::setHomeAddress);
        updateUserField(userEditProfileRequest::phoneNumber, user::getPhoneNumber, user::setPhoneNumber);
        updateUserField(userEditProfileRequest::bulstat, user::getBulstat, user::setBulstat);
        updateUserField(userEditProfileRequest::jobTitle, user::getJobTitle, user::setJobTitle);

        return UserTransformer.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse changePassword(String userId, UserChangePasswordRequest userChangePasswordRequest) {
        User user = getUserByIdOrThrowException(userId);
        String newPassword = userChangePasswordRequest.newPassword();
        String oldPassword = userChangePasswordRequest.oldPassword();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Given old password does not match!");
        }
        if (!newPassword.equals(userChangePasswordRequest.confirmNewPassword())) {
            throw new BadRequestException("New password and confirm password does not match!");
        }
        if (!passwordEncoder.matches(newPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user = userRepository.save(user);
        }

        return UserTransformer.toResponse(user);
    }

    @Transactional
    public UserResponse getMyInformation() {
        String email = jwtService.extractEmailFromToken();

        return UserTransformer.toResponse(getUserByEmailOrThrowException(email));
    }

    private void updateUserField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    public User getUserByEmailOrThrowException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_ENTITY, EMAIL_FIELD_NAME, email));
    }

    private User getUserByIdOrThrowException(String userId) {
        return userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException(USER_ENTITY, UUID_FIELD_NAME, userId));
    }
}
