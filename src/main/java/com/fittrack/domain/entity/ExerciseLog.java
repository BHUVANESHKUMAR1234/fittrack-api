package com.fittrack.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Individual exercise performed within a workout session.
 */
@Entity
@Table(name = "exercise_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @Column(name = "exercise_name", nullable = false, length = 100)
    private String exerciseName;

    @Column(name = "sets")
    private Integer sets;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
}
