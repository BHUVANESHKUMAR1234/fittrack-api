package com.fittrack.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ProgressSummaryResponse {
    private int totalWorkouts;
    private int totalMinutesExercised;
    private int totalCaloriesBurned;
    private int currentStreakDays;
    private int longestStreakDays;
    private double averageWorkoutsPerWeek;
    private Map<String, Integer> workoutsByType;     // e.g. { STRENGTH: 12, CARDIO: 5 }
    private Map<String, Integer> workoutsByMonth;    // e.g. { "2024-01": 8 }
    private NutritionSummary nutritionThisWeek;

    @Getter
    @Builder
    public static class NutritionSummary {
        private int totalCalories;
        private double totalProteinG;
        private double totalCarbsG;
        private double totalFatG;
        private double averageDailyCalories;
    }
}
