package com.ufrn.imd.divide.ai.dto.response;

import com.ufrn.imd.divide.ai.model.User;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        String color,
        Boolean expense,
        User userId
) {
}

