package com.darshil.backend.repository;

import com.darshil.backend.model.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {

    @Query("SELECT rr FROM RaceResult rr WHERE rr.race.id = :raceId")
    List<RaceResult> findByRaceId(Long raceId); // ✅ Add this
}
