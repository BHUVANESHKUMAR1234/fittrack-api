package com.fittrack.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExerciseLogRequest {

    @NotBlank(message = "Exercise name is required")
    @Size(max = 100)
    private String exerciseName;

    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;

    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    private Double weightKg;

    private Integer durationSeconds;

    private Double distanceKm;

    @NotNull(message = "Order index is required")
    @Min(0)
    private Integer orderIndex;
}
