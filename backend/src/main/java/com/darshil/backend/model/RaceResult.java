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

    // Link to Race entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    // Link to Driver entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    // Link to Team entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer position;
    private Double points;
    private String status;
    private String time;
    private String fastestLap;
}
