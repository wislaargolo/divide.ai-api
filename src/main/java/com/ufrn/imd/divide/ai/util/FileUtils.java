package com.ufrn.imd.divide.ai.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static String readSystemPromptFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        Path path = Paths.get(resource.getURI());

        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
