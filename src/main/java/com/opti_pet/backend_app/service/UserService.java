package com.opti_pet.backend_app.service;

import com.opti_pet.backend_app.exception.BadRequestException;
import com.opti_pet.backend_app.exception.NotFoundException;
import com.opti_pet.backend_app.persistence.model.Clinic;
import com.opti_pet.backend_app.persistence.model.Role;
import com.opti_pet.backend_app.persistence.model.User;
import com.opti_pet.backend_app.persistence.model.UserRoleClinic;
import com.opti_pet.backend_app.persistence.repository.ClinicRepository;
import com.opti_pet.backend_app.persistence.repository.UserRepository;
import com.opti_pet.backend_app.rest.request.clinic.ClinicCreateUserRequest;
import com.opti_pet.backend_app.rest.request.user.*;
import com.opti_pet.backend_app.rest.response.UserResponse;
import com.opti_pet.backend_app.rest.transformer.UserTransformer;
import com.opti_pet.backend_app.util.specifications.UserSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRoleClinicService userRoleClinicService;
    private final ClinicRepository clinicRepository;
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
        if (userRepository.existsByEmail(userRegisterRequest.email())) {
            throw new BadRequestException("User with this email already exists!");
        }
        User user = UserTransformer.toEntity(userRegisterRequest, passwordEncoder.encode(userRegisterRequest.password()));

        user = userRepository.save(user);

        Clinic clinic = clinicRepository.findById(DEFAULT_CLINIC_UUID)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, DEFAULT_CLINIC_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);

        List<UserRoleClinic> userRoleClinics = userRoleClinicService.saveNewUserRoleClinic(user, clinic, role);
        user.setUserRoleClinics(userRoleClinics);

        return UserTransformer.toResponse(userRepository.save(user));
    }

    @Transactional
    public User registerUserAsManager(ClinicCreateUserRequest clinicCreateUserRequest) {
        User user = UserTransformer.toEntity(clinicCreateUserRequest, passwordEncoder.encode(clinicCreateUserRequest.userPhoneNumber()));
        user = userRepository.save(user);

        Clinic clinic = clinicRepository.findById(DEFAULT_CLINIC_UUID)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, DEFAULT_CLINIC_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);
        List<UserRoleClinic> userRoleClinics = userRoleClinicService.saveNewUserRoleClinic(user, clinic, role);
        user.setUserRoleClinics(userRoleClinics);

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
        updateUserField(userEditProfileRequest::note, user::getNote, user::setNote);

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

    @Transactional
    public User getUserByEmailOrThrowException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_ENTITY, EMAIL_FIELD_NAME, email));
    }

    @Transactional
    public UserResponse registerUserAsAdmin(UserRegisterAsAdminRequest userRegisterAsAdminRequest) {
        if (userRepository.existsByEmail(userRegisterAsAdminRequest.email())) {
            throw new BadRequestException("User with this email already exists!");
        }
        User user = UserTransformer.toEntity(userRegisterAsAdminRequest, passwordEncoder.encode(userRegisterAsAdminRequest.phoneNumber()));

        user = userRepository.save(user);

        Clinic clinic = clinicRepository.findById(DEFAULT_CLINIC_UUID)
                .orElseThrow(() -> new NotFoundException(CLINIC_ENTITY, UUID_FIELD_NAME, DEFAULT_CLINIC_UUID.toString()));
        Role role = roleService.getRoleByIdOrThrowException(1L);

        List<UserRoleClinic> userRoleClinics = userRoleClinicService.saveNewUserRoleClinic(user, clinic, role);
        user.setUserRoleClinics(userRoleClinics);

        return UserTransformer.toResponse(userRepository.save(user));
    }

    @Transactional
    public Page<UserResponse> getAllUsers(UserSpecificationRequest userSpecificationRequest) {
        Pageable pageRequest = createPageRequest(userSpecificationRequest);

        return userRepository.findAll(getSpecifications(userSpecificationRequest), pageRequest).map(UserTransformer::toResponse);
    }

    private Specification<User> getSpecifications(UserSpecificationRequest userSpecificationRequest) {
        String inputText = userSpecificationRequest.inputText();
        Specification<User> specification = Specification.where(null);
        if (inputText != null) {
            specification = specification.and(UserSpecifications.userEmailOrFullNameOrPhoneNumberLike(inputText));
        }

        return specification;
    }

    private void updateUserField(Supplier<String> newField, Supplier<String> currentField, Consumer<String> updateField) {
        String newValue = newField.get();
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentField.get())) {
            updateField.accept(newValue);
        }
    }

    private User getUserByIdOrThrowException(String userId) {
        return userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException(USER_ENTITY, UUID_FIELD_NAME, userId));
    }

    private Pageable createPageRequest(UserSpecificationRequest request) {
        int pageNumber = request.pageNumber() != null ? request.pageNumber() : DEFAULT_PAGE_NUMBER;
        int pageSize = request.pageSize() != null ? request.pageSize() : DEFAULT_PAGE_SIZE;

        return PageRequest.of(pageNumber, pageSize);
    }
}
