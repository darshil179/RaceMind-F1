package com.darshil.backend.service;

import com.darshil.backend.model.Driver;
import com.darshil.backend.model.Race;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ErgastService {

    private final WebClient webClient;

    public ErgastService(@Value("${ergast.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Cacheable(value = "driverStandings", key = "#season")
    public List<Driver> getDriverStandings(int season) {
        log.info("Fetching driver standings for season: {}", season);

        try {
            Map<String, Object> response = webClient.get()
                    .uri("/{season}/driverStandings.json", season)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return parseDriverStandings(response);
        } catch (Exception e) {
            log.error("Error fetching driver standings", e);
            return List.of();
        }
    }

    @Cacheable(value = "raceSchedule", key = "#season")
    public List<Race> getRaceSchedule(int season) {
        log.info("Fetching race schedule for season: {}", season);

        try {
            Map<String, Object> response = webClient.get()
                    .uri("/{season}.json", season)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return parseRaceSchedule(response, season);
        } catch (Exception e) {
            log.error("Error fetching race schedule", e);
            return List.of();
        }
    }

    private List<Driver> parseDriverStandings(Map<String, Object> response) {
        // Parse Ergast API response and convert to Driver entities
        // Implementation depends on actual API response structure
        // This is a placeholder - you'll need to implement actual parsing
        return List.of();
    }

    private List<Race> parseRaceSchedule(Map<String, Object> response, int season) {
        // Parse Ergast API response and convert to Race entities
        // This is a placeholder - you'll need to implement actual parsing
        return List.of();
    }
}