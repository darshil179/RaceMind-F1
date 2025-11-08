package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_standings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStanding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private String teamId;
    private String teamName;
    private Double points;
    private Integer position;
}
