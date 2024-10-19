package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(

        @NotNull
        String name,
        String description,
        @NotBlank
        String color,
        @NotBlank
        Boolean isExpense,
        @NotBlank
        Long userId
) {
}

