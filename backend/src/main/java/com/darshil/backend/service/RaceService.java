package com.darshil.backend.service;

import com.darshil.backend.model.Race;
import com.darshil.backend.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RaceService {

    @Autowired
    private RaceRepository raceRepository;

    public Race getNextRace() {
        return raceRepository.findNextRace(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No upcoming races found"));
    }
}
