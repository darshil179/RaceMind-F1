package com.darshil.backend.repository;

import com.darshil.backend.model.TeamStanding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamStandingRepository extends JpaRepository<TeamStanding, Long> {
    List<TeamStanding> findBySeasonOrderByPositionAsc(Integer season);
}
