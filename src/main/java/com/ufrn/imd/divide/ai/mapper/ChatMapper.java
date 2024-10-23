package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.OpenAIRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.OpenAIResponseDTO;
import com.ufrn.imd.divide.ai.model.Chat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    Chat toEntity(OpenAIResponseDTO openAIResponseDTO);
    Chat toEntity(OpenAIRequestDTO openAIRequestDTO);
    OpenAIResponseDTO toDto(Chat chat);
}
