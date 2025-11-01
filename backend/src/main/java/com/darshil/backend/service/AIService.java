package com.darshil.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class AIService {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    public AIService(
            @Value("${ai.api-key}") String apiKey,
            @Value("${ai.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getRaceInsight(String query, String context) {
        log.info("Getting AI insight for query: {}", query);

        try {
            String prompt = buildPrompt(query, context);

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", new Object[] {
                            Map.of(
                                    "role", "system",
                                    "content", "You are an F1 racing expert providing insights and predictions."
                            ),
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    }
            );

            Map<String, Object> response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return extractResponse(response);

        } catch (Exception e) {
            log.error("Error getting AI insight", e);
            return "Unable to generate insights at this time. Please try again later.";
        }
    }

    private String buildPrompt(String query, String context) {
        return String.format("""
            Context: %s
            
            Question: %s
            
            Provide a brief, informative answer about F1 racing based on the context and question.
            Keep the response under 100 words.
            """, context, query);
    }

    private String extractResponse(Map<String, Object> response) {
        try {
            var choices = (java.util.List<?>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                var choice = (Map<?, ?>) choices.get(0);
                var message = (Map<?, ?>) choice.get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            log.error("Error extracting AI response", e);
        }
        return "Unable to process response.";
    }

    public String generateDriverInsight(String driverName, int points, String team) {
        String context = String.format(
                "Driver: %s, Team: %s, Points: %d",
                driverName, team, points
        );

        String query = "Provide a brief insight about this driver's performance and strengths.";

        return getRaceInsight(query, context);
    }
}