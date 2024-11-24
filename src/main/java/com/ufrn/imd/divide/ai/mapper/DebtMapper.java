package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.DebtRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.DebtUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.DebtResponseDTO;
import com.ufrn.imd.divide.ai.model.Debt;
import com.ufrn.imd.divide.ai.model.GroupTransaction;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupTransactionMapper.class})
public interface DebtMapper {
    DebtResponseDTO toDTO(Debt debt);

    @Mapping(target = "user", ignore = true)
    Debt toEntity(DebtRequestDTO dto);

    Debt toEntity(DebtUpdateRequestDTO dto);

    @Mapping(target = "groupTransaction", expression = "java(savedGroupTransaction)")
    Debt toEntity(DebtRequestDTO debtRequestDTO, @Context GroupTransaction savedGroupTransaction);
}
