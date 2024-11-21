package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Date;

public record DebtResponseDTO(
        Long id,
        Double amount,
        UserResponseDTO user,
        Date createdAt,
        Date paidAt
) {
}
