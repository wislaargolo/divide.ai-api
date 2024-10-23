package com.ufrn.imd.divide.ai.dto.response;

import java.time.LocalDateTime;

public record OpenAIResponseDTO(Long userId, String response, LocalDateTime createdAt) {
}
