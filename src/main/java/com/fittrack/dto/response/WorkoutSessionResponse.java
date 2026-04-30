package com.fittrack.dto.response;

import com.fittrack.domain.enums.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class WorkoutSessionResponse {
    private UUID id;
    private String name;
    private WorkoutType workoutType;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private String notes;
    private LocalDateTime createdAt;
    private List<ExerciseLogResponse> exercises;
}
