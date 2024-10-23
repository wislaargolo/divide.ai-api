package com.ufrn.imd.divide.ai.dto.request;

import java.time.LocalDateTime;

public record UserTransactionUpdateRequestDTO(
        Double amount,
        String description,
        Long categoryId,
        LocalDateTime paidAt

) {
}
