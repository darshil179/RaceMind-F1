package com.darshil.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_standings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverStanding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private String driverId;
    private String driverName;
    private String teamId;
    private Double points;
    private Integer position;
}
