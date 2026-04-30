package com.fittrack.controller;

import com.fittrack.dto.response.ApiResponse;
import com.fittrack.dto.response.ProgressSummaryResponse;
import com.fittrack.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Progress analytics and streak endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Progress", description = "Fitness progress analytics")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/summary")
    @Operation(summary = "Get complete progress summary — workouts, calories, nutrition, streak")
    public ResponseEntity<ApiResponse<ProgressSummaryResponse>> getSummary(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("GET /progress/summary - user: {}", userDetails.getUsername());
        ProgressSummaryResponse response = progressService.getSummary(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/streak/update")
    @Operation(summary = "Recalculate streak for the authenticated user")
    public ResponseEntity<ApiResponse<Void>> updateStreak(
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("POST /progress/streak/update - user: {}", userDetails.getUsername());
        progressService.updateStreak(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.message("Streak updated"));
    }
}
