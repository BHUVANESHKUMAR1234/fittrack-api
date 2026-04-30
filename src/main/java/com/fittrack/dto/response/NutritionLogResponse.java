package com.fittrack.dto.response;

import com.fittrack.domain.enums.MealType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class NutritionLogResponse {
    private UUID id;
    private LocalDate logDate;
    private MealType mealType;
    private String foodName;
    private Double quantityGrams;
    private Integer calories;
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private LocalDateTime createdAt;
}
