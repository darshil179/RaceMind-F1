package com.darshil.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "teams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    private String teamId; // e.g. "red_bull"
    private String teamName;
    private String nationality;
    private Integer firstAppearance;
    private Integer constructorsChampionships;
    private Integer driversChampionships;
    private String wikiUrl;
}
