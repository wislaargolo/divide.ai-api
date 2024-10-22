package com.ufrn.imd.divide.ai.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.ufrn.imd.divide.ai.dto.request.OpenAIRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.OpenAIResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.mapper.ChatMapper;
import com.ufrn.imd.divide.ai.model.Chat;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.OpenAIRepository;
import com.ufrn.imd.divide.ai.repository.UserRepository;
import com.ufrn.imd.divide.ai.service.interfaces.IOpenAIService;

import com.ufrn.imd.divide.ai.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIservice implements IOpenAIService {
    private OpenAIClient openAIClient;
    private final ChatMapper chatMapper;
    private OpenAIRepository openAIRepository;
    private UserRepository userRepository;
    @Value("${openai.model}")
    private String model;

    @Value("${openai.temperature}")
    private Double temperature;

    private static final String SYSTEM_PROMPT_FILE_PATH = "prompt/SystemPrompt.txt";

    public OpenAIservice(OpenAIClient openAIClient, ChatMapper chatMapper, OpenAIRepository openAIRepository, UserRepository userRepository) {
        this.openAIClient = openAIClient;
        this.chatMapper = chatMapper;
        this.openAIRepository = openAIRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OpenAIResponseDTO chatCompletion(OpenAIRequestDTO chatRequestDTO) throws Exception {
        Long userId = chatRequestDTO.userId();
        String prompt = chatRequestDTO.prompt();

        List<ChatRequestMessage> chatMessages = buildChatMessages(chatRequestDTO.prompt());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Error("Usuário com ID '" + userId + "' não encontrado."));

        ChatCompletionsOptions options = new ChatCompletionsOptions(chatMessages)
                .setTemperature(temperature);

        var chatCompletionResponse = openAIClient.getChatCompletions(model, options);

        if (chatCompletionResponse.getChoices().isEmpty() || chatCompletionResponse.getChoices().get(0).getMessage().getContent().isEmpty())
            throw new BusinessException("Empty response from OpenAI", HttpStatus.BAD_REQUEST);

        String chatResponse = chatCompletionResponse.getChoices().get(0).getMessage().getContent();
        String chatId = chatCompletionResponse.getId();

        Chat chat = chatMapper.toEntity(chatRequestDTO);
        chat.setUser(user);
        chat.setResponse(chatResponse);
        chat.setChatId(chatId);

        openAIRepository.save(chat);

        return chatMapper.toDto(chat);
    }

    private List<ChatRequestMessage> buildChatMessages(String prompt) throws Exception {
        List<ChatRequestMessage> chatMessages = new ArrayList<>();

        String systemPromptTemplate = FileUtils.readSystemPromptFile(SYSTEM_PROMPT_FILE_PATH);

        chatMessages.add(new ChatRequestSystemMessage(systemPromptTemplate));
        chatMessages.add(new ChatRequestUserMessage(prompt));

        return chatMessages;
    }

    @Override
    public OpenAIResponseDTO getLastChat(Long userId) {
        Chat lastChat = openAIRepository.findLatestChatByUserId(userId);

        if (lastChat == null) return null;

        return chatMapper.toDto(lastChat);
    }
}
