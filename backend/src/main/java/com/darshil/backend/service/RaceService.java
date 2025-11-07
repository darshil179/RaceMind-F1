package com.darshil.backend.service;

import org.springframework.stereotype.Service;
import com.darshil.backend.model.Race;
import java.time.LocalDate;

@Service
public class RaceService {

    public Race getNextRace() {
        // Placeholder data — will replace with Ergast API later
        Race nextRace = new Race();
        nextRace.setRaceName("São Paulo Grand Prix");
        nextRace.setCircuitName("Interlagos Circuit");
        nextRace.setCountry("Brazil");
        nextRace.setDate(LocalDate.now().plusDays(10).toString());
        nextRace.setTime("14:00 UTC");
        return nextRace;
    }
}
