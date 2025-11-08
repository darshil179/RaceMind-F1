package com.darshil.backend.repository;

import com.darshil.backend.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface RaceRepository extends JpaRepository<Race, Long> {

    @Query("SELECT r FROM Race r WHERE r.date >= :today ORDER BY r.date ASC LIMIT 1")
    Optional<Race> findNextRace(LocalDate today);

    Optional<Race> findByRaceId(String raceId); //for F1SyncService
}
