package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OpenAIRequestDTO(
        Long userId,
        String prompt) {
}
