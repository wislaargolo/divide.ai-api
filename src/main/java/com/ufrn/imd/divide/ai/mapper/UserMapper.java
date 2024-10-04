package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.UserRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDTO UserRequestDTO);

    UserResponseDTO toDto(User user);
}
