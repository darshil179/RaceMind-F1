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
public class ErgastF1Service {

    private final RaceRepository raceRepository;
    private final DriverRepository driverRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public void fetchAndSaveRaces() {
        String url = "https://ergast.com/api/f1/current.json";
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode races = root.path("MRData").path("RaceTable").path("Races");

            for (JsonNode r : races) {
                String round = r.path("round").asText();
                String raceName = r.path("raceName").asText();
                String date = r.path("date").asText();
                String time = r.path("time").asText(null);

                Race race = Race.builder()
                        .name(raceName)
                        .round(Integer.parseInt(round))
                        .date(LocalDate.parse(date))
                        .time(time != null ? LocalTime.parse(time.replace("Z", "")) : null)
                        .build();

                raceRepository.save(race);
            }
            System.out.println("✅ Races fetched and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchAndSaveDrivers() {
        String url = "https://ergast.com/api/f1/current/drivers.json";
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode drivers = root.path("MRData").path("DriverTable").path("Drivers");

            for (JsonNode d : drivers) {
                Driver driver = Driver.builder()
                        .driverId(d.path("driverId").asText())
                        .firstName(d.path("givenName").asText())
                        .lastName(d.path("familyName").asText())
                        .nationality(d.path("nationality").asText())
                        .build();

                driverRepository.save(driver);
            }
            System.out.println("✅ Drivers fetched and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
