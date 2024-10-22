package com.ufrn.imd.divide.ai.config;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.KeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIRestTemplate {
    @Value("${openai.key}")
    private String openAIKey;

    @Value("${openai.endpoint}")
    private String openAIEndpoint;

    @Bean
    public OpenAIClient openAIClient() {
        return new OpenAIClientBuilder()
                .credential(new KeyCredential(openAIKey))
                .buildClient();
    }
}
