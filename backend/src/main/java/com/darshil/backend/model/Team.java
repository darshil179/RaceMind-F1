package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamId;       // API team ID (e.g., mercedes)
    private String name;         // full name (e.g., Mercedes AMG Petronas)
    private String nationality;

    private Integer firstAppearance;
    private Integer constructorsChampionships;
    private Integer driversChampionships;
    private String country;

    private String base; // 🆕 added for team headquarters/base info

    private Integer points;
    private Integer position;
    private String wikiUrl;
}
