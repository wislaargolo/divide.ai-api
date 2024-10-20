package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.CategoryRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "user", ignore = true)
    Category toEntity (CategoryRequestDTO categoryDTO);

    CategoryResponseDTO toDto(Category category);
}
