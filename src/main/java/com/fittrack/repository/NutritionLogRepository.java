package com.fittrack.repository;

import com.fittrack.domain.entity.NutritionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {

    Page<NutritionLog> findByUserIdOrderByLogDateDesc(UUID userId, Pageable pageable);

    Optional<NutritionLog> findByIdAndUserId(UUID id, UUID userId);

    List<NutritionLog> findByUserIdAndLogDateOrderByMealType(UUID userId, LocalDate logDate);

    @Query("SELECT n FROM NutritionLog n WHERE n.user.id = :userId " +
           "AND n.logDate BETWEEN :from AND :to ORDER BY n.logDate DESC")
    List<NutritionLog> findByUserIdAndDateRange(UUID userId, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(n.calories), 0) FROM NutritionLog n " +
           "WHERE n.user.id = :userId AND n.logDate BETWEEN :from AND :to")
    int sumCaloriesByUserIdAndDateRange(UUID userId, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(n.proteinG), 0.0) FROM NutritionLog n " +
           "WHERE n.user.id = :userId AND n.logDate BETWEEN :from AND :to")
    double sumProteinByUserIdAndDateRange(UUID userId, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(n.carbsG), 0.0) FROM NutritionLog n " +
           "WHERE n.user.id = :userId AND n.logDate BETWEEN :from AND :to")
    double sumCarbsByUserIdAndDateRange(UUID userId, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(n.fatG), 0.0) FROM NutritionLog n " +
           "WHERE n.user.id = :userId AND n.logDate BETWEEN :from AND :to")
    double sumFatByUserIdAndDateRange(UUID userId, LocalDate from, LocalDate to);
}
