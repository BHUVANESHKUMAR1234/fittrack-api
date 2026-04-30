package com.fittrack.service;

import com.fittrack.domain.entity.ExerciseLog;
import com.fittrack.domain.entity.User;
import com.fittrack.domain.entity.WorkoutSession;
import com.fittrack.dto.request.WorkoutSessionRequest;
import com.fittrack.dto.response.WorkoutSessionResponse;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.mapper.WorkoutMapper;
import com.fittrack.repository.WorkoutSessionRepository;
import com.fittrack.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Business logic for workout session management.
 * Handles creation, retrieval, update, deletion of workout sessions and exercises.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutSessionRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final UserService userService;

    @Transactional
    public WorkoutSessionResponse createWorkout(String email, WorkoutSessionRequest request) {
        log.info("Creating workout '{}' for user: {}", request.getName(), email);
        User user = userService.findByEmail(email);

        WorkoutSession session = workoutMapper.toEntity(request);
        session.setUser(user);

        // Map and attach exercises with back-reference
        if (request.getExercises() != null) {
            List<ExerciseLog> exercises = request.getExercises().stream()
                    .map(ex -> {
                        ExerciseLog log = workoutMapper.toEntity(ex);
                        log.setWorkoutSession(session);
                        return log;
                    })
                    .toList();
            session.getExerciseLogs().addAll(exercises);
        }

        // Auto-calculate duration if not provided
        if (session.getDurationMinutes() == null
                && session.getStartedAt() != null
                && session.getEndedAt() != null) {
            long minutes = java.time.Duration.between(
                    session.getStartedAt(), session.getEndedAt()).toMinutes();
            session.setDurationMinutes((int) minutes);
        }

        WorkoutSession saved = workoutRepository.save(session);
        log.info("Workout created: {}", saved.getId());
        return workoutMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutSessionResponse> getWorkouts(String email, int page, int size) {
        User user = userService.findByEmail(email);
        int safeSize = Math.min(size, AppConstants.MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, safeSize);
        return workoutRepository
                .findByUserIdOrderByStartedAtDesc(user.getId(), pageable)
                .map(workoutMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public WorkoutSessionResponse getWorkoutById(String email, UUID workoutId) {
        User user = userService.findByEmail(email);
        WorkoutSession session = workoutRepository
                .findByIdAndUserId(workoutId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found: " + workoutId));
        return workoutMapper.toResponse(session);
    }

    @Transactional
    public WorkoutSessionResponse updateWorkout(String email, UUID workoutId,
                                                WorkoutSessionRequest request) {
        log.info("Updating workout {} for user: {}", workoutId, email);
        User user = userService.findByEmail(email);
        WorkoutSession session = workoutRepository
                .findByIdAndUserId(workoutId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found: " + workoutId));

        workoutMapper.updateFromRequest(request, session);

        // Replace exercises entirely
        session.getExerciseLogs().clear();
        if (request.getExercises() != null) {
            request.getExercises().stream()
                    .map(ex -> {
                        ExerciseLog el = workoutMapper.toEntity(ex);
                        el.setWorkoutSession(session);
                        return el;
                    })
                    .forEach(session.getExerciseLogs()::add);
        }

        WorkoutSession saved = workoutRepository.save(session);
        log.info("Workout updated: {}", saved.getId());
        return workoutMapper.toResponse(saved);
    }

    @Transactional
    public void deleteWorkout(String email, UUID workoutId) {
        log.info("Deleting workout {} for user: {}", workoutId, email);
        User user = userService.findByEmail(email);
        if (!workoutRepository.existsByIdAndUserId(workoutId, user.getId())) {
            throw new ResourceNotFoundException("Workout not found: " + workoutId);
        }
        workoutRepository.deleteById(workoutId);
        log.info("Workout deleted: {}", workoutId);
    }
}
