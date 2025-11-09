package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "races")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation with RaceResult
    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RaceResult> results;

    private String raceId; // e.g. "australian_2025"
    private String raceName;
    private Integer round;
    private Integer season;
    private LocalDate date;
    private LocalTime time;
    private String circuitName;
    private String country;
    private String city;
    private String circuitLength;
    private String wikiUrl;
}
