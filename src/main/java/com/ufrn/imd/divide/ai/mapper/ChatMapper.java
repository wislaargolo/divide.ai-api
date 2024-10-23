package com.ufrn.imd.divide.ai.mapper;

import com.ufrn.imd.divide.ai.dto.request.OpenAIRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.OpenAIResponseDTO;
import com.ufrn.imd.divide.ai.model.Chat;
import com.ufrn.imd.divide.ai.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    Chat toEntity(OpenAIResponseDTO openAIResponseDTO);
    Chat toEntity(OpenAIRequestDTO openAIRequestDTO);
    @Mapping(source = "user.id", target = "userId")
    OpenAIResponseDTO toDto(Chat chat);
}
