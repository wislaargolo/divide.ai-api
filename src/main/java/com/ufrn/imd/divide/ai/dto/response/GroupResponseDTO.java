package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GroupResponseDTO(
        Long id,
        String name,
        String description,
        String code,
        List<UserResponseDTO> members,
        UserResponseDTO createdBy,
        boolean discontinued
) {
}
