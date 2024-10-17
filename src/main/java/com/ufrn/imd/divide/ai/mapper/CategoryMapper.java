package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.CategoryDTO;
import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity (CategoryDTO categoryDTO);

    CategoryDTO toDto(Category category);
}
