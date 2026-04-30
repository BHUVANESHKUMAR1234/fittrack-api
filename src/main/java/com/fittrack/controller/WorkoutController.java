package com.fittrack.controller;

import com.fittrack.dto.request.WorkoutSessionRequest;
import com.fittrack.dto.response.ApiResponse;
import com.fittrack.dto.response.WorkoutSessionResponse;
import com.fittrack.service.WorkoutService;
import com.fittrack.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Workout session CRUD endpoints.
 * All endpoints require a valid JWT.
 */
@Slf4j
@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Workouts", description = "Workout session management")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    @Operation(summary = "Log a new workout session")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody WorkoutSessionRequest request) {
        log.info("POST /workouts - user: {}", userDetails.getUsername());
        WorkoutSessionResponse response = workoutService.createWorkout(
                userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Workout logged successfully"));
    }

    @GetMapping
    @Operation(summary = "Get paginated list of user's workouts")
    public ResponseEntity<ApiResponse<Page<WorkoutSessionResponse>>> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("GET /workouts - user: {}, page: {}", userDetails.getUsername(), page);
        Page<WorkoutSessionResponse> response = workoutService.getWorkouts(
                userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific workout session by ID")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id) {
        log.debug("GET /workouts/{} - user: {}", id, userDetails.getUsername());
        WorkoutSessionResponse response = workoutService.getWorkoutById(
                userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a workout session")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutSessionRequest request) {
        log.info("PUT /workouts/{} - user: {}", id, userDetails.getUsername());
        WorkoutSessionResponse response = workoutService.updateWorkout(
                userDetails.getUsername(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Workout updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a workout session")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id) {
        log.info("DELETE /workouts/{} - user: {}", id, userDetails.getUsername());
        workoutService.deleteWorkout(userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.message("Workout deleted successfully"));
    }
}
