package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserUpdateRequestDTO userUpdateRequestDTO);

    User toEntity(UserCreateRequestDTO userCreateRequestDTO);

    UserResponseDTO toDto(User user);
}
