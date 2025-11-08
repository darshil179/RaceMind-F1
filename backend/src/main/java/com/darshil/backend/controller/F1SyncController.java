package com.darshil.backend.controller;

import com.darshil.backend.model.DriverStanding;
import com.darshil.backend.model.TeamStanding;
import com.darshil.backend.repository.DriverStandingRepository;
import com.darshil.backend.repository.TeamStandingRepository;
import com.darshil.backend.service.F1SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/f1")
@RequiredArgsConstructor
public class F1SyncController {

    private final F1SyncService syncService;
    private final DriverStandingRepository driverStandingRepository;
    private final TeamStandingRepository teamStandingRepository;

    @GetMapping("/sync")
    public String syncSeasonsNow(@RequestParam(name="seasons", required=false) List<Integer> seasons) {
        if (seasons == null || seasons.isEmpty()) seasons = List.of(2024, 2025);
        syncService.syncSeasons(seasons, 25);
        return "✅ Sync started for seasons: " + seasons;
    }

    @GetMapping("/standings/drivers/{season}")
    public Collection<DriverStanding> getDriverStandings(@PathVariable Integer season) {
        return driverStandingRepository.findBySeasonOrderByPositionAsc(season);
    }

    @GetMapping("/standings/teams/{season}")
    public Collection<TeamStanding> getTeamStandings(@PathVariable Integer season) {
        return teamStandingRepository.findBySeasonOrderByPositionAsc(season);
    }
}
