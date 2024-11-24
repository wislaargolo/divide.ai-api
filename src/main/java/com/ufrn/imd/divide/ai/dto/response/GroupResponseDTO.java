package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record GroupResponseDTO(
        Long id,
        String name,
        String description,
        String code,
        List<UserResponseDTO> members,
        UserResponseDTO createdBy,
        boolean discontinued,
        Date createdAt
) {
}
