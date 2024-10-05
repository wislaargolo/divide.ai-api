package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;


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
        @Pattern(regexp = "^\\([1-9]{2}\\) (?:[2-8]|9[0-9])[0-9]{3}\\-[0-9]{4}$", message = "The phone number must be in the format (XX) XXXXX-XXXX.")
        String phoneNumber
) {
}
