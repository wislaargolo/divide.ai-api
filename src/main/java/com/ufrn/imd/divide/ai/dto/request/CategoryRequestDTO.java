package com.ufrn.imd.divide.ai.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequestDTO (
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String color
){}
