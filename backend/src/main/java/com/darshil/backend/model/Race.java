package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "races")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raceId; // e.g. "bahrain_gp_2024"
    private String raceName;
    private String circuitName;
    private String country;

    private LocalDate date; // <— add this (race date)
    private LocalTime time; // <— add this (race time)

    private Integer round;
    private String season;
}
