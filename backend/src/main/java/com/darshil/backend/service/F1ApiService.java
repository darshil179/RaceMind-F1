package com.darshil.backend.service;

import com.darshil.backend.model.Driver;
import com.darshil.backend.model.Race;
import com.darshil.backend.repository.DriverRepository;
import com.darshil.backend.repository.RaceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class F1ApiService {

    private final DriverRepository driverRepository;
    private final RaceRepository raceRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches and saves all 2024 F1 drivers.
     */
    public void fetchAndSaveDrivers() {
        String url = "https://f1api.dev/api/2024/drivers";
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode drivers = root.path("drivers");

            for (JsonNode d : drivers) {
                Driver driver = Driver.builder()
                        .driverId(d.path("driverId").asText())
                        .firstName(d.path("name").asText())
                        .lastName(d.path("surname").asText())
                        .nationality(d.path("nationality").asText())
                        .birthday(d.path("birthday").asText())
                        .team(d.path("teamId").asText())
                        .number(d.path("number").asInt())
                        .shortName(d.path("shortName").asText())
                        .wikiUrl(d.path("url").asText())
                        .build();

                driverRepository.save(driver);
            }

            System.out.println("✅ F1 2024 Drivers saved successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error fetching drivers:");
            e.printStackTrace();
        }
    }

    /**
     * Fetches and saves a specific race (e.g., Round 1 of 2025 season).
     */
    public void fetchAndSaveRace() {
        String url = "https://f1api.dev/api/2025/1/race";
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode raceNode = root.path("races");

            Race race = Race.builder()
                    .round(Integer.parseInt(raceNode.path("round").asText()))
                    .raceName(raceNode.path("raceName").asText())
                    .raceId(raceNode.path("raceId").asText())
                    .date(LocalDate.parse(raceNode.path("date").asText()))
                    .time(LocalTime.parse(raceNode.path("time").asText().replace("Z", "")))
                    .country(raceNode.path("circuit").path("country").asText())
                    .city(raceNode.path("circuit").path("city").asText())
                    .circuitName(raceNode.path("circuit").path("circuitName").asText())
                    .circuitLength(raceNode.path("circuit").path("circuitLength").asText())
                    .wikiUrl(raceNode.path("url").asText())
                    .build();

            raceRepository.save(race);

            System.out.println("✅ F1 Race " + race.getRaceName() + " saved successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error fetching race:");
            e.printStackTrace();
        }
    }

    /**
     * Combined method to sync everything.
     */
    public void syncAll() {
        fetchAndSaveDrivers();
        fetchAndSaveRace();
    }
}
