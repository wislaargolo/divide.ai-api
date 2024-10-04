package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
