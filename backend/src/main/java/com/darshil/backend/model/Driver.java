package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driverId;      // F1 API driver ID
    private String firstName;
    private String lastName;
    private String fullName;
    private String shortName;
    private Integer number;
    private String nationality;
    private String birthday;
    private Integer points;
    private Integer position;
    private String wikiUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Long getTeamId() {
        return team != null ? team.getId() : null;
    }
}
