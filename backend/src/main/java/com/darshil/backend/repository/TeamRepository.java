package com.darshil.backend.repository;

import com.darshil.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamId(String teamId);
}

