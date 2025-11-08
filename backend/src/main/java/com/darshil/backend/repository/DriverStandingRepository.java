package com.darshil.backend.repository;

import com.darshil.backend.model.DriverStanding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverStandingRepository extends JpaRepository<DriverStanding, Long> {
    List<DriverStanding> findBySeasonOrderByPositionAsc(Integer season);
    Optional<DriverStanding> findBySeasonAndDriverId(Integer season, String driverId);
}
