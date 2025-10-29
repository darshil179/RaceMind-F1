package com.darshil.backend.controller;

import com.darshil.backend.repository.DriverRepository;
import com.darshil.backend.model.Driver;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:5173")
public class PredictionController {

    private final DriverRepository repo;
    public PredictionController(DriverRepository repo) { this.repo = repo; }

    @GetMapping("/title-chances")
    public Map<String, Double> calculateChances() {
        List<Driver> drivers = repo.findAll();
        int remainingRaces = 5;
        int maxPointsPerRace = 25;

        int maxPossible = drivers.stream().mapToInt(Driver::getPoints).max().orElse(0)
                + remainingRaces * maxPointsPerRace;

        Map<String, Double> chances = new LinkedHashMap<>();
        for (Driver d : drivers) {
            double chance = (double) (d.getPoints() + (remainingRaces * 18)) / maxPossible * 100;
            chances.put(d.getName(), Math.round(chance * 100.0) / 100.0);
        }
        return chances;
    }
}
