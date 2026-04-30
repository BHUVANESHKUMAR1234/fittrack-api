package com.fittrack.dto.request;

import com.fittrack.domain.enums.WorkoutType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class WorkoutSessionRequest {

    @NotBlank(message = "Workout name is required")
    @Size(max = 100)
    private String name;

    @NotNull(message = "Workout type is required")
    private WorkoutType workoutType;

    @NotNull(message = "Start time is required")
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer durationMinutes;

    private Integer caloriesBurned;

    @Size(max = 500)
    private String notes;

    @Valid
    private List<ExerciseLogRequest> exercises = new ArrayList<>();
}
