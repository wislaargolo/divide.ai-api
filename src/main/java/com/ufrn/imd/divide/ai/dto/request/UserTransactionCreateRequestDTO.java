package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserTransactionCreateRequestDTO(
        @NotNull(message = "Amount é obrigatório.")
        Double amount,
        @NotBlank(message = "Description é obrigatório.")
        String description,
        @NotNull(message = "CategoryId é obrigatório.")
        Long categoryId,
        @NotNull(message = "UserId é obrigatório.")
        Long userId,
        LocalDateTime paidAt
) {
}
