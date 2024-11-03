package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.GroupTransactionCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.GroupTransactionResponseDTO;
import com.ufrn.imd.divide.ai.model.GroupTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DebtMapper.class})
public interface GroupTransactionMapper {

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    GroupTransaction toEntity(GroupTransactionCreateRequestDTO dto);
    GroupTransactionResponseDTO toDTO(GroupTransaction groupTransaction);
}
