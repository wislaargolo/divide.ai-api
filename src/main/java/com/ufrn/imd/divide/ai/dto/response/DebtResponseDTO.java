package com.ufrn.imd.divide.ai.dto.response;

public record DebtResponseDTO(
        Long id,
        Double amount,
        UserResponseDTO user
) {
}
