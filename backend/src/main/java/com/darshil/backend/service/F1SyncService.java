package com.darshil.backend.service;

import com.darshil.backend.model.*;
import com.darshil.backend.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class F1SyncService {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final RaceRepository raceRepository;
    private final TeamStandingRepository teamStandingRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "https://api.openf1.org/v1";

    /**
     * Fetches and saves drivers for a specific season.
     */
    @Transactional
    public void fetchAndSaveDriversForSeason(int season) {
        String url = BASE_URL + "/drivers?session_key=" + season;
        log.info("Fetching drivers for season {}", season);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode d : root) {
                String driverId = d.path("driverId").asText(null);
                if (driverId == null) continue;

                // Link with Team entity if exists
                String teamApiId = d.path("teamId").asText(null);
                Team team = null;
                if (teamApiId != null) {
                    team = teamRepository.findByTeamId(teamApiId).orElse(null);
                }

                Driver drv = Driver.builder()
                        .driverId(driverId)
                        .firstName(d.path("name").asText(""))
                        .lastName(d.path("surname").asText(""))
                        .nationality(d.path("nationality").asText(""))
                        .birthday(d.path("birthday").asText(""))
                        .number(d.path("number").isMissingNode() ? null : d.path("number").asInt())
                        .shortName(d.path("shortName").asText(null))
                        .wikiUrl(d.path("url").asText(null))
                        .team(team)
                        .build();

                driverRepository.save(drv);
            }

            log.info("Drivers for season {} saved successfully", season);

        } catch (Exception e) {
            log.error("Failed to fetch or save drivers for season {}: {}", season, e.getMessage());
        }
    }

    /**
     * Fetches race data and related results for a given season.
     */
    @Transactional
    public void fetchAndSaveRaceAndResults(int season) {
        String url = BASE_URL + "/races?season=" + season;
        log.info("Fetching races and results for season {}", season);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode raceNode : root) {
                String raceName = raceNode.path("raceName").asText(null);
                if (raceName == null) continue;

                Race race = Race.builder()
                        .season(season)
                        .raceName(raceName)
                        .round(raceNode.path("round").asInt())
                        .date(raceNode.path("date").asText(null))
                        .time(raceNode.path("time").asText(null))
                        .circuitName(raceNode.path("Circuit").path("circuitName").asText(null))
                        .location(raceNode.path("Circuit").path("Location").path("locality").asText(null))
                        .country(raceNode.path("Circuit").path("Location").path("country").asText(null))
                        .build();

                raceRepository.save(race);

                // Save teams for each race
                JsonNode results = raceNode.path("Results");
                if (results.isArray()) {
                    for (JsonNode res : results) {
                        String teamId = res.path("Constructor").path("constructorId").asText(null);
                        if (teamId != null) {
                            JsonNode teamNode = res.path("Constructor");
                            Team team = teamRepository.findByTeamId(teamId).orElse(
                                    Team.builder()
                                            .teamId(teamId)
                                            .name(teamNode.path("name").asText(null))
                                            .nationality(teamNode.path("nationality").asText(null))
                                            .firstAppearance(teamNode.path("firstAppearance").isMissingNode() ? null : teamNode.path("firstAppearance").asInt())
                                            .constructorsChampionships(teamNode.path("constructorsChampionships").isMissingNode() ? null : teamNode.path("constructorsChampionships").asInt())
                                            .driversChampionships(teamNode.path("driversChampionships").isMissingNode() ? null : teamNode.path("driversChampionships").asInt())
                                            .wikiUrl(teamNode.path("url").asText(null))
                                            .build()
                            );
                            teamRepository.save(team);
                        }
                    }
                }
            }

            log.info("Races and teams for season {} saved successfully", season);

        } catch (Exception e) {
            log.error("Failed to fetch or save race data for season {}: {}", season, e.getMessage());
        }
    }

    /**
     * Computes and stores team standings for a given season.
     */
    @Transactional
    public void computeAndSaveStandings(int season) {
        log.info("Computing and saving standings for season {}", season);
        try {
            var teams = teamRepository.findAll();
            int pos = 1;
            for (Team team : teams) {
                Integer pts = team.getPoints() != null ? team.getPoints() : 0;
                TeamStanding ts = TeamStanding.builder()
                        .season(season)
                        .teamId(team.getTeamId())
                        .teamName(team.getName())
                        .points(pts)
                        .position(pos++)
                        .build();
                teamStandingRepository.save(ts);
            }
            log.info("Team standings for season {} saved successfully", season);
        } catch (Exception e) {
            log.error("Failed to compute standings for season {}: {}", season, e.getMessage());
        }
    }
}
