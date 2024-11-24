package com.ufrn.imd.divide.ai.dto.response;

public record UserTransactionByCategoryDTO(
        Long categoryId,
        String categoryName,
        Long userId,

        double amount,
        int month,
        int year,
        String color
) {
}
