package com.ufrn.imd.divide.ai.dto.response;

import java.util.List;

public record GroupTransactionResponseDTO(
        Long id,
        Double amount,
        String description,
        GroupResponseDTO group,
        UserResponseDTO createdBy,
        List<DebtResponseDTO> debts
) {
}
