package com.ufrn.imd.divide.ai.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GroupTransactionUpdateRequestDTO(
        Double amount,
        String description,
        @NotEmpty(message = "debts é obrigatório")
        @Valid
        List<DebtUpdateRequestDTO> debts,
        LocalDate dueDate
) {
}
