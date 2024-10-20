package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDTO(
        @NotBlank(message = "firstName é obrigatório.")
        String firstName,
        @NotBlank(message = "lastName é obrigatório.")
        String lastName,
        @NotBlank(message = "email é obrigatório.")
        @Email
        String email,
        @NotBlank(message = "password é obrigatório.")
        String password,
        @NotBlank(message = "phoneNumber é obrigatório.")
        String phoneNumber
) {
}
