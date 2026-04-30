package com.fittrack.repository;

import com.fittrack.domain.entity.WorkoutSession;
import com.fittrack.domain.enums.WorkoutType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {

    Page<WorkoutSession> findByUserIdOrderByStartedAtDesc(UUID userId, Pageable pageable);

    Optional<WorkoutSession> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT w FROM WorkoutSession w WHERE w.user.id = :userId " +
           "AND w.startedAt BETWEEN :from AND :to ORDER BY w.startedAt DESC")
    List<WorkoutSession> findByUserIdAndDateRange(UUID userId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT COUNT(w) FROM WorkoutSession w WHERE w.user.id = :userId")
    int countByUserId(UUID userId);

    @Query("SELECT COALESCE(SUM(w.durationMinutes), 0) FROM WorkoutSession w WHERE w.user.id = :userId")
    int sumDurationByUserId(UUID userId);

    @Query("SELECT COALESCE(SUM(w.caloriesBurned), 0) FROM WorkoutSession w WHERE w.user.id = :userId")
    int sumCaloriesByUserId(UUID userId);

    @Query("SELECT w.workoutType, COUNT(w) FROM WorkoutSession w WHERE w.user.id = :userId GROUP BY w.workoutType")
    List<Object[]> countByWorkoutTypeForUser(UUID userId);

    @Query("SELECT FUNCTION('TO_CHAR', w.startedAt, 'YYYY-MM'), COUNT(w) " +
           "FROM WorkoutSession w WHERE w.user.id = :userId " +
           "GROUP BY FUNCTION('TO_CHAR', w.startedAt, 'YYYY-MM') ORDER BY 1")
    List<Object[]> countByMonthForUser(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
