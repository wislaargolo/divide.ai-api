package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.dto.request.OpenAIRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.OpenAIResponseDTO;
import jakarta.validation.Valid;
public interface IOpenAIService {
    OpenAIResponseDTO chatCompletion(@Valid OpenAIRequestDTO chatRequestDTO) throws Exception;
    OpenAIResponseDTO getLastChat(Long userId);
}
