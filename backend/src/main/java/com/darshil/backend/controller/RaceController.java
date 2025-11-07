package com.darshil.backend.controller;

import com.darshil.backend.model.Race;
import com.darshil.backend.service.RaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/races")
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping("/next")
    public Race getNextRace() {
        return raceService.getNextRace();
    }

    @GetMapping
    public List<Race> getAllRaces() {
        return raceService.getAllRaces();
    }
}
