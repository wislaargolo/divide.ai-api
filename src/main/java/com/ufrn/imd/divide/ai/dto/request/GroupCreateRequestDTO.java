package com.ufrn.imd.divide.ai.dto.request;

import com.ufrn.imd.divide.ai.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GroupCreateRequestDTO(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotNull(message = "Creator is required")
        Long createdBy
) {
}

