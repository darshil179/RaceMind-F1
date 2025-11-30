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
                Team team = null;

                if (teamApiId != null) {
                    team = teamRepository.findByTeamId(teamApiId).orElse(null);

                    if (team == null) {
                        team = Team.builder()
                                .teamId(teamApiId)
                                .name(d.path("team_name").asText())
                                .country(d.path("country_code").asText(null))
                                .build();
                        teamRepository.save(team);
                    }
                }


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

            if (root.isEmpty()) {
                // fallback to sessions API (works with session_key)
                log.warn("⚠️ No races found for season {}. Trying sessions endpoint...", season);
                url = BASE_URL + "/sessions?year=" + season;
                json = restTemplate.getForObject(url, String.class);
                root = mapper.readTree(json);
            }

            if (root.isEmpty()) {
                log.error("❌ Still no data found for season {}", season);
                return;
            }

            for (JsonNode raceNode : root) {
                String raceName = raceNode.path("meeting_name").asText(null);
                if (raceName == null || raceName.isEmpty()) raceName = raceNode.path("session_name").asText(null);
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
                        .round(raceNode.path("round_number").asInt(0))
                        .date(date)
                        .circuitName(raceNode.path("circuit_short_name").asText(null))
                        .country(raceNode.path("country_name").asText(null))
                        .city(raceNode.path("location").asText(null))
                        .build();

                Race savedRace = raceRepository.save(race);

                fetchAndSaveResultsForRace(savedRace.getId());


                // Optionally extract and save team info if present
                if (raceNode.has("team_name")) {
                    String teamName = raceNode.path("team_name").asText(null);
                    if (teamName != null && teamRepository.findByName(teamName).isEmpty()) {
                        Team team = Team.builder()
                                .name(teamName)
                                .teamId(teamName.toLowerCase().replace(" ", "_"))
                                .build();
                        teamRepository.save(team);
                    }
                }
            }

            log.info("✅ Races for season {} saved successfully", season);
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

        var races = raceRepository.findBySeason(season);
        var results = raceResultRepository.findByRaceIdIn(
                races.stream().map(Race::getId).toList()
        );

        Map<String, Double> teamPoints = new HashMap<>();

        for (RaceResult r : results) {
            teamPoints.merge(r.getTeamId(), r.getPoints(), Double::sum);
        }

        int pos = 1;
        for (var entry : teamPoints.entrySet()) {
            Team team = teamRepository.findByTeamId(entry.getKey()).orElse(null);
            if (team == null) continue;

            TeamStanding ts = TeamStanding.builder()
                    .season(season)
                    .teamId(team.getTeamId())
                    .teamName(team.getName())
                    .points(entry.getValue())
                    .position(pos++)
                    .build();

            teamStandingRepository.save(ts);
        }
    }


    @Transactional
    public void fetchAndSaveTeams() {
        String url = BASE_URL + "/teams";
        log.info("Fetching teams...");

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode node : root) {
                String teamId = node.path("team_id").asText(null);
                if (teamId == null) continue;

                if (teamRepository.findByTeamId(teamId).isPresent()) continue;

                Team team = Team.builder()
                        .teamId(teamId)
                        .name(node.path("team_name").asText(null))
                        .country(node.path("country_name").asText(null))
                        .base(node.path("base").asText(null))
                        .build();

                teamRepository.save(team);
            }

            log.info("✅ Teams saved successfully");
        } catch (Exception e) {
            log.error("Error fetching/saving teams: {}", e.getMessage());
        }
    }


    /**
     * Sync multiple seasons.
     */
    /**
     * Sync multiple seasons.
     */
    @Transactional
    public void syncSeasons(List<Integer> seasons, int maxRounds) {

        fetchAndSaveTeams(); // <-- IMPORTANT

        for (Integer season : seasons) {
            log.info("=== Syncing season {} ===", season);

            fetchAndSaveDriversForSeason(season);
            sleep();

            fetchAndSaveRaceAndResults(season);
            sleep();

            computeAndSaveStandings(season);
        }
    }

    private void sleep() {
        try { Thread.sleep(1200); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Transactional
    public void fetchAndSaveResultsForRace(Long raceId) {
        String url = BASE_URL + "/results?race_id=" + raceId;

        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);

            for (JsonNode node : root) {
                String driverId = node.path("driver_id").asText(null);
                if (driverId == null) continue;

                String teamId = node.path("team_id").asText(null);

                RaceResult result = RaceResult.builder()
                        .raceId(raceId)
                        .driverId(driverId)
                        .teamId(teamId)
                        .position(node.path("position").asInt())
                        .points(node.path("points").asDouble(0))
                        .build();

                raceResultRepository.save(result);
            }

            log.info("Saved results for race {}", raceId);

        } catch (Exception e) {
            log.error("Error saving results for race {}: {}", raceId, e.getMessage());
        }
    }


}
