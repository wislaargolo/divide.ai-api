package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotNull;

public record DebtRequestDTO(
        @NotNull(message = "userId de dívida é obrigatório.")
        Long userId,
        @NotNull(message = "amount de dívida é obrigatório.")
        Double amount
) {
}
