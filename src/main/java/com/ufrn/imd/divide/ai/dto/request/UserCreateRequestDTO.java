package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserCreateRequestDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String email,
        @NotBlank
        String password,
        String phoneNumber
) {
}
