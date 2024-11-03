package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GroupTransactionCreateRequestDTO(
        @NotNull(message = "amount é obrigatório.")
        Double amount,
        @NotNull(message = "groupId é obrigatório.")
        Long groupId,
        @NotBlank(message = "description é obrigatório.")
        String description,
        @NotNull(message = "createdByUserId é obrigatório.")
        Long createdByUserId,
        List<DebtRequestDTO> debts
) {
}
