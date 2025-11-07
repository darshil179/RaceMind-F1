package com.darshil.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.darshil.backend.model.Race;
import com.darshil.backend.service.RaceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/race")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // frontend Vite default
public class RaceController {

    private final RaceService raceService;

    @GetMapping("/next")
    public Race getNextRace() {
        return raceService.getNextRace();
    }
}
