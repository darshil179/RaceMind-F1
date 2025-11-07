package com.darshil.backend.controller;

import com.darshil.backend.model.RaceResult;
import com.darshil.backend.service.RaceResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class RaceResultController {

    private final RaceResultService raceResultService;

    public RaceResultController(RaceResultService raceResultService) {
        this.raceResultService = raceResultService;
    }

    @GetMapping("/{raceId}")
    public List<RaceResult> getResultsForRace(@PathVariable Long raceId) {
        return raceResultService.getResultsForRace(raceId);
    }
}
