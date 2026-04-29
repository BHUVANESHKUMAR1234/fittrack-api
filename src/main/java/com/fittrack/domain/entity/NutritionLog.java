package com.fittrack.domain.entity;

import com.fittrack.domain.enums.MealType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Daily nutrition entry logged by the user.
 */
@Entity
@Table(name = "nutrition_logs", indexes = {
    @Index(name = "idx_nutrition_user_date", columnList = "user_id, log_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    @Column(name = "food_name", nullable = false, length = 150)
    private String foodName;

    @Column(name = "quantity_grams", nullable = false)
    private Double quantityGrams;

    @Column(name = "calories", nullable = false)
    private Integer calories;

    @Column(name = "protein_g")
    private Double proteinG;

    @Column(name = "carbs_g")
    private Double carbsG;

    @Column(name = "fat_g")
    private Double fatG;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
