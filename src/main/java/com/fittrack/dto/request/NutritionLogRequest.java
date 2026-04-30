package com.fittrack.dto.request;

import com.fittrack.domain.enums.MealType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class NutritionLogRequest {

    @NotNull(message = "Log date is required")
    private LocalDate logDate;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotBlank(message = "Food name is required")
    @Size(max = 150)
    private String foodName;

    @NotNull
    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    private Double quantityGrams;

    @NotNull
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;

    @DecimalMin("0.0")
    private Double proteinG;

    @DecimalMin("0.0")
    private Double carbsG;

    @DecimalMin("0.0")
    private Double fatG;
}
