package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GroupUpdateRequestDTO(
        String name,
        String description
) {
}
