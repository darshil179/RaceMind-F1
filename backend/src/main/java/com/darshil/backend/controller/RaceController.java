package com.darshil.backend.controller;

import com.darshil.backend.model.Race;
import com.darshil.backend.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/races")
@CrossOrigin(origins = "http://localhost:5173") // frontend port (vite)
public class RaceController {

    @Autowired
    private RaceService raceService;

    @GetMapping("/next")
    public Race getNextRace() {
        return raceService.getNextRace();
    }
}