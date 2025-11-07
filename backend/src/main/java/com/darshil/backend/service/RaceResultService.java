package com.darshil.backend.service;

import com.darshil.backend.model.RaceResult;
import com.darshil.backend.repository.RaceResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceResultService {

    private final RaceResultRepository raceResultRepository;

    public RaceResultService(RaceResultRepository raceResultRepository) {
        this.raceResultRepository = raceResultRepository;
    }

    public List<RaceResult> getResultsForRace(Long raceId) {
        return raceResultRepository.findByRaceId(raceId);
    }
}
