package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotNull;

public record DebtUpdateRequestDTO(
        @NotNull(message = "id de dívida é obrigatório.")
        Long id,
        @NotNull(message = "amount de dívida é obrigatório.")
        Double amount
) {}