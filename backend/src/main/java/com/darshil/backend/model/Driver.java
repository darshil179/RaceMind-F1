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

    private String driverId;
    private String firstName;
    private String lastName;
    private String nationality;

    private String team;
    private int points;
    private int position;

    private String birthday;  //
    private String teamId;    //  for linking to team
}
