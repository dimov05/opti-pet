package com.opti_pet.backend_app.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserChangePasswordRequest (@NotBlank String oldPassword,@NotBlank String newPassword,@NotBlank String confirmNewPassword){
}
