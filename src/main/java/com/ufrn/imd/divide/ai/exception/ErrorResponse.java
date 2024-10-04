package com.ufrn.imd.divide.ai.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(
        ZonedDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path) {
}
