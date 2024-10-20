package com.ufrn.imd.divide.ai.dto.response;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;

import java.time.LocalDateTime;

public record UserTransactionResponseDTO(
        Long id,
        Double amount,
        String description,
        CategoryRequestDTO category,
        LocalDateTime paidAt
) {
}
