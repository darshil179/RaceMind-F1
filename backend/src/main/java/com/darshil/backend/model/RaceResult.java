package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "race_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driverId;
    private String teamId;
    private Long raceDbId; // FK to Race.id (nullable until race is saved)
    private Integer season;
    private Integer round;
    private Integer position;   // finishing position (nullable if NC)
    private Double points;      // points scored in this race
    private Integer grid;       // grid position
    private String time;        // race time string or gap
    private String status;      // e.g. "Finished", "DNF"
}
