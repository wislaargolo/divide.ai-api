package com.ufrn.imd.divide.ai.dto.response;

public record UserTransactionByMonthResponseDTO(
        int month,
        int year,
        double totalIncome,
        double totalExpenses
) {
}
