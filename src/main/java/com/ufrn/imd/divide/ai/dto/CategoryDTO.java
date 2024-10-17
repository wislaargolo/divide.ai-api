package com.ufrn.imd.divide.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        Long id,
        String name,
        String description,
        String color
) {
}

