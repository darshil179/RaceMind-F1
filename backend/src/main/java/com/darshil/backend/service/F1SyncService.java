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

    @Transactional
    public void fetchAndSaveDriversForSeason(int season) {
        String url = BASE_URL + "/drivers?session_key=" + season;
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
                        .birthday(d.path("dob").asText(null))
                        .team(team)
                        .build();

                driverRepository.save(driver);
            }

            log.info("Drivers for season {} saved successfully", season);
        } catch (Exception e) {
            log.error("Error fetching/saving drivers for season {}: {}", season, e.getMessage());
        }
    }

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
                if (!raceNode.path("date_start").isMissingNode()) {
                    date = LocalDate.parse(raceNode.path("date_start").asText().substring(0, 10));
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

                if (raceNode.has("teams")) {
                    for (JsonNode teamNode : raceNode.path("teams")) {
                        String teamId = teamNode.path("team_id").asText(null);
                        if (teamId == null) continue;

                        Optional<Team> existingTeam = teamRepository.findByTeamId(teamId);
                        if (existingTeam.isEmpty()) {
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

    @Transactional
    public void computeAndSaveStandings(int season) {
        log.info("Computing standings for season {}", season);
        try {
            var teams = teamRepository.findAll();
            int pos = 1;
            for (Team team : teams) {
                Double pts = team.getPoints() != null ? team.getPoints() : 0.0;
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

    @Transactional
    public void syncSeasons(List<Integer> seasons, int maxRounds) {
        for (Integer season : seasons) {
            fetchAndSaveDriversForSeason(season);
            fetchAndSaveRaceAndResults(season);
            computeAndSaveStandings(season);
        }
    }
}
