package com.ufrn.imd.divide.ai.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufrn.imd.divide.ai.dto.request.OpenAIRequestDTO;
import com.ufrn.imd.divide.ai.dto.response.CategoryResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.OpenAIResponseDTO;
import com.ufrn.imd.divide.ai.dto.response.UserTransactionResponseDTO;
import com.ufrn.imd.divide.ai.exception.BusinessException;
import com.ufrn.imd.divide.ai.mapper.ChatMapper;
import com.ufrn.imd.divide.ai.model.Chat;
import com.ufrn.imd.divide.ai.model.User;
import com.ufrn.imd.divide.ai.repository.OpenAIRepository;
import com.ufrn.imd.divide.ai.service.interfaces.*;

import com.ufrn.imd.divide.ai.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final IUserService userService;
    private final IUserTransactionService userTransactionService;
    private final ICategoryService categoryService;
    private final IUserValidationService userValidationService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.temperature}")
    private Double temperature;

    private static final String SYSTEM_PROMPT_FILE_PATH = "prompt/SystemPrompt.txt";

    private final Logger logger = LoggerFactory.getLogger(OpenAIservice.class);

    public OpenAIservice(OpenAIClient openAIClient,
                         ChatMapper chatMapper,
                         OpenAIRepository openAIRepository,
                         IUserService userService,
                         IUserTransactionService userTransactionService, ICategoryService categoryService, IUserValidationService userValidationService) {
        this.openAIClient = openAIClient;
        this.chatMapper = chatMapper;
        this.openAIRepository = openAIRepository;
        this.userService = userService;
        this.userTransactionService = userTransactionService;
        this.categoryService = categoryService;
        this.userValidationService = userValidationService;
    }

    @Override
    public OpenAIResponseDTO chatCompletion(OpenAIRequestDTO chatRequestDTO) throws Exception {
        Long userId = chatRequestDTO.userId();
        String userPrompt = chatRequestDTO.prompt();

        userValidationService.validateUser(userId);
        User user = userService.findById(userId);

        String objectivePrompt = buildUserObjectivePrompt(userPrompt);
        String transactionPrompt = buildUserTransactionPrompt(userId);
        String categoriesTransactionPrompt = buildUserCategoriesTransactionPrompt(userId);

        String prompt = objectivePrompt + "\n" + transactionPrompt + "\n" + categoriesTransactionPrompt;

        // logger.info("Prompt: " + prompt);

        List<ChatRequestMessage> chatMessages = buildChatMessages(prompt);

        ChatCompletionsOptions options = new ChatCompletionsOptions(chatMessages)
                .setTemperature(temperature);

        var chatCompletionResponse = openAIClient.getChatCompletions(model, options);

        if (chatCompletionResponse.getChoices().isEmpty() || chatCompletionResponse.getChoices().get(0).getMessage().getContent().isEmpty())
            throw new BusinessException("Empty response from OpenAI", HttpStatus.BAD_REQUEST);

        String chatResponse = chatCompletionResponse.getChoices().get(0).getMessage().getContent();
        String chatId = chatCompletionResponse.getId();

        // logger.info("chatResponse: " + chatResponse);

        OpenAIResponseDTO responseDTO = parseChatResponseToJSON(chatResponse);

        Chat chat = chatMapper.toEntity(chatRequestDTO);
        chat.setUser(user);
        chat.setResponse(chatResponse);
        chat.setChatId(chatId);

        openAIRepository.save(chat);

        return responseDTO;
    }

    private List<ChatRequestMessage> buildChatMessages(String prompt) throws Exception {
        List<ChatRequestMessage> chatMessages = new ArrayList<>();

        String systemPromptTemplate = FileUtils.readSystemPromptFile(SYSTEM_PROMPT_FILE_PATH);

        chatMessages.add(new ChatRequestSystemMessage(systemPromptTemplate));
        chatMessages.add(new ChatRequestUserMessage(prompt));

        return chatMessages;
    }

    private String buildUserObjectivePrompt(String prompt) {
        if (prompt == null || prompt.isEmpty()) {
            return "";
        }
        return String.format("Meu objetivo é: %s\n", prompt);
    }

    private String buildUserTransactionPrompt(Long userId) {
        try {
            List<UserTransactionResponseDTO> userTransactions = userTransactionService.findAllByUserId(userId);

            if (userTransactions == null || userTransactions.isEmpty()) {
                return "Lista de transações: []\n";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String userTransactionsJson = objectMapper.writeValueAsString(userTransactions);

            return String.format("Lista de transações: \n%s", userTransactionsJson);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao construir a lista de transações.", HttpStatus.BAD_REQUEST);
        }
    }

    private String buildUserCategoriesTransactionPrompt(Long userId) {
        try {
            List<CategoryResponseDTO> categories = categoryService.getCategoriesByUserId(userId);

            if (categories == null || categories.isEmpty()) {
                return "Lista de categorias: []\n";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String categoriesJson = objectMapper.writeValueAsString(categories);

            return String.format("Lista de categorias: \n%s", categoriesJson);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao construir a lista de categorias.", HttpStatus.BAD_REQUEST);
        }
    }

    private OpenAIResponseDTO parseChatResponseToJSON(String chatResponse) {
        String cleanedResponse = chatResponse.replaceAll("```json|```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(cleanedResponse, OpenAIResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao parsear resposta da LLM para JSON.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public OpenAIResponseDTO getLastChat(Long userId) {
        Chat lastChat = openAIRepository.findTopByUserIdOrderByCreatedAtDesc(userId);

        if (lastChat == null) return null;

        OpenAIResponseDTO lastChatResponse = parseChatResponseToJSON(lastChat.getResponse());

        return lastChatResponse;
    }

}
