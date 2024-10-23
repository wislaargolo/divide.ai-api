package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.UserCreateRequestDTO;
import com.ufrn.imd.divide.ai.dto.request.UserUpdateRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.UserResponseDTO;
import com.ufrn.imd.divide.ai.model.User;

public interface IUserService {
    void delete(Long userId);

    UserResponseDTO update(UserUpdateRequestDTO dto, Long userId);

    UserResponseDTO save(UserCreateRequestDTO dto);

    User findById(Long userId);
}
