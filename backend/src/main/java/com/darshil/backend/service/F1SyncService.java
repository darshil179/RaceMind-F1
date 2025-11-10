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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Fetch drivers for a given season and store them in the DB.
     */
    @Transactional
    public void fetchAndSaveDriversForSeason(int season) {
        String url = BASE_URL + "/drivers?year=" + season;
        log.info("Fetching drivers for season {}", season);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode d : root) {
                String driverId = d.path("driver_id").asText(null);
                if (driverId == null) continue;

                String teamApiId = d.path("team_id").asText(null);
                Team team = teamApiId != null
                        ? teamRepository.findByTeamId(teamApiId).orElse(null)
                        : null;

                Driver driver = Driver.builder()
                        .driverId(driverId)
                        .firstName(d.path("first_name").asText(""))
                        .lastName(d.path("last_name").asText(""))
                        .shortName(d.path("short_name").asText(""))
                        .number(d.path("driver_number").isMissingNode() ? null : d.path("driver_number").asInt())
                        .nationality(d.path("country_code").asText(""))
                        .wikiUrl(d.path("url").asText(null))
                        .birthday(d.hasNonNull("dob") ? d.path("dob").asText() : null)
                        .team(team)
                        .build();

                driverRepository.save(driver);
            }

            log.info("Drivers for season {} saved successfully", season);
        } catch (Exception e) {
            log.error("Error fetching/saving drivers for season {}: {}", season, e.getMessage());
        }
    }

    /**
     * Fetch races for a given season, including circuits and teams.
     */
    @Transactional
    public void fetchAndSaveRaceAndResults(int season) {
        String url = BASE_URL + "/races?year=" + season;
        log.info("Fetching races for season {}", season);

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode raceNode : root) {
                String raceName = raceNode.path("meeting_name").asText(null);
                if (raceName == null) continue;

                LocalDate date = null;
                if (raceNode.hasNonNull("date_start")) {
                    String dateText = raceNode.path("date_start").asText().substring(0, 10);
                    try {
                        date = LocalDate.parse(dateText, DATE_FORMATTER);
                    } catch (Exception e) {
                        log.warn("Invalid date format for race {}: {}", raceName, dateText);
                    }
                }

                Race race = Race.builder()
                        .season(season)
                        .raceName(raceName)
                        .round(raceNode.path("round_number").asInt())
                        .date(date)
                        .circuitName(raceNode.path("circuit_short_name").asText(null))
                        .country(raceNode.path("country_name").asText(null))
                        .build();

                raceRepository.save(race);

                // Save related teams if present
                if (raceNode.has("teams")) {
                    for (JsonNode teamNode : raceNode.path("teams")) {
                        String teamId = teamNode.path("team_id").asText(null);
                        if (teamId == null) continue;

                        if (teamRepository.findByTeamId(teamId).isEmpty()) {
                            Team team = Team.builder()
                                    .teamId(teamId)
                                    .name(teamNode.path("team_name").asText(null))
                                    .nationality(teamNode.path("country_code").asText(null))
                                    .wikiUrl(teamNode.path("url").asText(null))
                                    .build();
                            teamRepository.save(team);
                        }
                    }
                }
            }

            log.info("Races and teams for season {} saved successfully", season);
        } catch (Exception e) {
            log.error("Error fetching/saving races for season {}: {}", season, e.getMessage());
        }
    }

    /**
     * Compute and store team standings for a season.
     */
    @Transactional
    public void computeAndSaveStandings(int season) {
        log.info("Computing standings for season {}", season);
        try {
            var teams = teamRepository.findAll();
            int pos = 1;
            for (Team team : teams) {
                double pts = team.getPoints() != null ? team.getPoints() : 0.0;
                TeamStanding ts = TeamStanding.builder()
                        .season(season)
                        .teamId(team.getTeamId())
                        .teamName(team.getName())
                        .points(pts)
                        .position(pos++)
                        .build();
                teamStandingRepository.save(ts);
            }
            log.info("Standings saved successfully for season {}", season);
        } catch (Exception e) {
            log.error("Error computing standings for season {}: {}", season, e.getMessage());
        }
    }

    /**
     * Sync multiple seasons.
     */
    @Transactional
    public void syncSeasons(List<Integer> seasons, int maxRounds) {
        for (Integer season : seasons) {
            log.info("=== Syncing season {} ===", season);
            fetchAndSaveDriversForSeason(season);

            try {
                Thread.sleep(1200); // Rate limit buffer
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            fetchAndSaveRaceAndResults(season);

            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            computeAndSaveStandings(season);
        }
    }
}
