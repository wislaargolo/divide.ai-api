package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserTransactionResponseDTO(
        Long id,
        Double amount,
        String description,
        CategoryResponseDTO category,
        LocalDateTime paidAt,
        LocalDateTime createdAt


) {
}
