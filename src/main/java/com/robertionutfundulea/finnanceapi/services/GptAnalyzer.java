package com.robertionutfundulea.finnanceapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class GptAnalyzer {
    private final String api_key;

    public GptAnalyzer() {
        this.api_key = System.getenv("OPENAPI_KEY");
    }

    private String extractContentFromResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.has("choices")) {
                JsonNode choicesNode = rootNode.get("choices");
                if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                    JsonNode firstChoice = choicesNode.get(0);
                    if (firstChoice.has("message")) {
                        JsonNode messageNode = firstChoice.get("message");
                        if (messageNode.has("content")) {
                            return messageNode.get("content").asText();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while extracting content from response: {}", e.getMessage());
        }

        return null; // Return null if content extraction fails
    }

    public String getReview(String content) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + api_key)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            """
                            {
                                "model": "gpt-4",
                                "messages": [{"role": "user", "content": "%s"}],
                                "temperature": 0.1
                            }
                            """.formatted(content)
                    ))
                    .build();

            var client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return extractContentFromResponse(response.body());
        } catch (InterruptedException | IOException e) {
            log.error("Error while making HTTP request to GPT-3 API: {}", e.getMessage());
            return "Error occurred while processing the request.";
        }
    }
}
