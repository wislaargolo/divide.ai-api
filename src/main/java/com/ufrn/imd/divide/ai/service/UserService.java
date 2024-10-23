package com.ufrn.imd.divide.ai.service;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.User;

public interface UserService {
    void delete(Long userId);

    UserResponseDTO update(UserUpdateRequestDTO dto, Long userId);

    UserResponseDTO save(UserCreateRequestDTO dto);

    User findById(Long userId);
}
