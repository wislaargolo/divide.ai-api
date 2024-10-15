package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        String color
) {
}

