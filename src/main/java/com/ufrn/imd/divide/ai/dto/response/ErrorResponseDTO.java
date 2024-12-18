package com.ufrn.imd.divide.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public record ErrorResponseDTO(
        ZonedDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path) {
}
