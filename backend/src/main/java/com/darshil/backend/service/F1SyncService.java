package com.darshil.backend.service;

import com.darshil.backend.model.*;
import com.darshil.backend.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class F1SyncService {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final RaceRepository raceRepository;
    private final RaceResultRepository raceResultRepository;
    private final DriverStandingRepository driverStandingRepository;
    private final TeamStandingRepository teamStandingRepository;

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Sync drivers, races and race results across given seasons.
     * We'll attempt rounds 1..maxRounds for each season and skip if race not found.
     */
    @Transactional
    public void syncSeasons(Collection<Integer> seasons, int maxRounds) {
        for (Integer season : seasons) {
            fetchAndSaveDriversForSeason(season);
            fetchAllRacesAndResultsForSeason(season, maxRounds);
            computeAndSaveStandings(season);
        }
    }

    public void fetchAndSaveDriversForSeason(int season) {
        try {
            String url = String.format("https://f1api.dev/api/%d/drivers", season);
            String json = rest.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode drivers = root.path("drivers");
            if (!drivers.isArray()) return;

            for (JsonNode d : drivers) {
                String driverId = d.path("driverId").asText();
                Driver drv = Driver.builder()
                        .driverId(driverId)
                        .firstName(d.path("name").asText(""))
                        .lastName(d.path("surname").asText(""))
                        .nationality(d.path("nationality").asText(""))
                        .birthday(d.path("birthday").asText(""))
                        .teamId(d.path("teamId").asText(null))
                        .number(d.path("number").isMissingNode() ? null : d.path("number").asInt())
                        .shortName(d.path("shortName").asText(null))
                        .wikiUrl(d.path("url").asText(null))
                        .build();
                driverRepository.save(drv);
            }
        } catch (Exception e) {
            System.err.println("Error fetching drivers for season " + season);
            e.printStackTrace();
        }
    }

    public void fetchAllRacesAndResultsForSeason(int season, int maxRounds) {
        for (int round = 1; round <= maxRounds; round++) {
            fetchAndSaveRaceAndResults(season, round);
        }
    }

    public void fetchAndSaveRaceAndResults(int season, int round) {
        try {
            String url = String.format("https://f1api.dev/api/%d/%d/race", season, round);
            String json = rest.getForObject(url, String.class);
            JsonNode root = mapper.readTree(json);
            JsonNode racesNode = root.path("races");
            if (racesNode.isMissingNode() || racesNode.isNull()) return;

            // Save race
            Race race = Race.builder()
                    .raceId(racesNode.path("raceId").asText(null))
                    .raceName(racesNode.path("raceName").asText(null))
                    .round(racesNode.path("round").isMissingNode() ? round : racesNode.path("round").asInt())
                    .season(season)
                    .date(racesNode.path("date").isMissingNode() ? null : LocalDate.parse(racesNode.path("date").asText()))
                    .time(racesNode.path("time").isMissingNode() ? null : LocalTime.parse(racesNode.path("time").asText().replace("Z","")))
                    .circuitName(racesNode.path("circuit").path("circuitName").asText(null))
                    .country(racesNode.path("circuit").path("country").asText(null))
                    .city(racesNode.path("circuit").path("city").asText(null))
                    .circuitLength(racesNode.path("circuit").path("circuitLength").asText(null))
                    .wikiUrl(racesNode.path("url").asText(null))
                    .build();

            // upsert based on raceId if present
            if (race.getRaceId() != null) {
                Optional<Race> existing = raceRepository.findByRaceId(race.getRaceId());
                if (existing.isPresent()) {
                    Race e = existing.get();
                    race.setId(e.getId());
                }
            }
            Race savedRace = raceRepository.save(race);

            // Save results array if present
            JsonNode results = racesNode.path("results");
            if (results.isArray()) {
                for (JsonNode r : results) {
                    JsonNode driverNode = r.path("driver");
                    JsonNode teamNode = r.path("team");

                    String driverId = driverNode.path("driverId").asText(null);
                    String teamId = teamNode.path("teamId").asText(null);

                    // Save team minimal info
                    if (teamId != null) {
                        Team team = Team.builder()
                                .teamId(teamId)
                                .teamName(teamNode.path("teamName").asText(null))
                                .nationality(teamNode.path("nationality").asText(null))
                                .firstAppearance(teamNode.path("firstAppareance").isMissingNode() ? null : teamNode.path("firstAppareance").asInt())
                                .constructorsChampionships(teamNode.path("constructorsChampionships").isMissingNode() ? null : teamNode.path("constructorsChampionships").asInt())
                                .driversChampionships(teamNode.path("driversChampionships").isMissingNode() ? null : teamNode.path("driversChampionships").asInt())
                                .wikiUrl(teamNode.path("url").asText(null))
                                .build();
                        teamRepository.save(team);
                    }

                    RaceResult rr = RaceResult.builder()
                            .driverId(driverId)
                            .teamId(teamId)
                            .raceDbId(savedRace.getId())
                            .season(season)
                            .round(r.get("position").isMissingNode() ? null : (r.get("position").isTextual() ? null : r.path("position").asInt()))
                            .position(r.path("position").isMissingNode() ? null : (r.path("position").isTextual() ? null : r.path("position").asInt()))
                            .points(r.path("points").isMissingNode() ? 0.0 : r.path("points").asDouble(0.0))
                            .grid(r.path("grid").isMissingNode() ? null : (r.path("grid").isTextual() ? null : r.path("grid").asInt()))
                            .time(r.path("time").isMissingNode() ? null : r.path("time").asText(null))
                            .status(r.path("retired").isMissingNode() ? (r.path("status").asText(null)) : (r.path("retired").asText(null)))
                            .build();

                    raceResultRepository.save(rr);
                }
            }

        } catch (Exception e) {
            // If API responds 404 or no race, just skip
            // Uncomment for debugging:
            // System.err.println("No race for season " + season + " round " + round + " or error.");
            // e.printStackTrace();
        }
    }

    /**
     * Compute standings by aggregating saved race results for season.
     */
    @Transactional
    public void computeAndSaveStandings(int season) {
        // aggregate driver points
        List<RaceResult> seasonResults = raceResultRepository.findBySeason(season);

        Map<String, Double> driverPoints = new HashMap<>();
        Map<String, Double> teamPoints = new HashMap<>();
        Map<String, String> driverNameMap = new HashMap<>();
        Map<String, String> teamNameMap = new HashMap<>();

        for (RaceResult rr : seasonResults) {
            if (rr.getDriverId() == null) continue;
            driverPoints.put(rr.getDriverId(), driverPoints.getOrDefault(rr.getDriverId(), 0.0) + (rr.getPoints() == null ? 0.0 : rr.getPoints()));
            if (rr.getTeamId() != null) {
                teamPoints.put(rr.getTeamId(), teamPoints.getOrDefault(rr.getTeamId(), 0.0) + (rr.getPoints() == null ? 0.0 : rr.getPoints()));
            }
        }

        // Build and save driver standings
        List<Map.Entry<String, Double>> driverSorted = driverPoints.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());

        driverStandingRepository.deleteAllById(driverStandingRepository.findAll().stream().map(DriverStanding::getId).collect(Collectors.toList()));
        int pos = 1;
        for (Map.Entry<String, Double> e : driverSorted) {
            String driverId = e.getKey();
            Double pts = e.getValue();
            Driver drv = driverRepository.findById(driverId).orElse(null);
            DriverStanding ds = DriverStanding.builder()
                    .season(season)
                    .driverId(driverId)
                    .driverName(drv != null ? drv.getFirstName() + " " + drv.getLastName() : driverId)
                    .teamId(drv != null ? drv.getTeamId() : null)
                    .points(pts)
                    .position(pos++)
                    .build();
            driverStandingRepository.save(ds);
        }

        // Build and save team standings
        List<Map.Entry<String, Double>> teamSorted = teamPoints.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());

        teamStandingRepository.deleteAll();
        pos = 1;
        for (Map.Entry<String, Double> e : teamSorted) {
            String teamId = e.getKey();
            Double pts = e.getValue();
            Team team = teamRepository.findById(teamId).orElse(null);
            TeamStanding ts = TeamStanding.builder()
                    .season(season)
                    .teamId(teamId)
                    .teamName(team != null ? team.getTeamName() : teamId)
                    .points(pts)
                    .position(pos++)
                    .build();
            teamStandingRepository.save(ts);
        }
    }
}
