package com.skillup30.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "challenge_days", uniqueConstraints = @UniqueConstraint(columnNames = { "challenge_id", "day_number" }))
public class ChallengeDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false)
    private int dayNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String task;
}