package com.opti_pet.backend_app.rest.request;

import lombok.Builder;

@Builder
public record UserEditProfileRequest (String name, String phoneNumber, String homeAddress, String bulstat, String jobTitle){
}
