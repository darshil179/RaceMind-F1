package com.darshil.backend.service;

import com.darshil.backend.model.Race;
import com.darshil.backend.repository.RaceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    public Race getNextRace() {
        LocalDate today = LocalDate.now();
        Optional<Race> nextRace = raceRepository.findNextRace(today);
        return nextRace.orElse(null);
    }

    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }
}
