package org.example.smartattendencebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false, unique = true)
    private String sessionName;

    @Column(nullable = false)
    private Long startYear;

    @Column(nullable = false)
    private Long endYear;
}
