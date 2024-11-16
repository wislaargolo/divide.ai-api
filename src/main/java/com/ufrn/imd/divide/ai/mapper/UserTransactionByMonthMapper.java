package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.response.UserTransactionByMonthResponseDTO;
import com.ufrn.imd.divide.ai.model.VWUserTransactionsGroupedByMonth;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTransactionByMonthMapper {
    UserTransactionByMonthResponseDTO toDto(VWUserTransactionsGroupedByMonth vwUserTransactionsGroupedByMonth);
}
