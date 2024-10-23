package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.UserTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.model.UserTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTransactionMapper {

    UserTransaction toEntity(UserTransactionCreateRequestDTO userTransactionCreateRequestDTO);
    UserTransactionResponseDTO toDto(UserTransaction userTransaction);
}
