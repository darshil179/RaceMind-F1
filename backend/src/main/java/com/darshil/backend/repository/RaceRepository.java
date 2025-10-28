package com.darshil.backend.repository;

import com.darshil.backend.model.Race;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {
}
