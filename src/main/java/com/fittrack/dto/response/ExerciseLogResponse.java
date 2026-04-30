package com.fittrack.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ExerciseLogResponse {
    private UUID id;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Double weightKg;
    private Integer durationSeconds;
    private Double distanceKm;
    private Integer orderIndex;
}
