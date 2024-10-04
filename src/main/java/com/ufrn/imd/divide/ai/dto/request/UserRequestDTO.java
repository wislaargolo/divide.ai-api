package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;


public record UserRequestDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String phoneNumber
) {
}
