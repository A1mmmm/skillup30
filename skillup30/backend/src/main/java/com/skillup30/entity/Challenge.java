package com.skillup30.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int rewardPoints;

    @Column(nullable = false, length = 20)
    private String difficultyLevel;

    private String coverImage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String dailyTask;
}