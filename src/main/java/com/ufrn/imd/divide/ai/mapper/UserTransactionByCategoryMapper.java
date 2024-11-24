package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.response.UserTransactionByCategoryDTO;
import com.ufrn.imd.divide.ai.model.VWUserTransactionByCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTransactionByCategoryMapper {
    UserTransactionByCategoryDTO toDto(VWUserTransactionByCategory vwUserTransactionByCategory);
}
