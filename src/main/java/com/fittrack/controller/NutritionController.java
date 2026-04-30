package com.fittrack.controller;

import com.fittrack.dto.request.NutritionLogRequest;
import com.fittrack.dto.response.ApiResponse;
import com.fittrack.dto.response.NutritionLogResponse;
import com.fittrack.service.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Nutrition log CRUD endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/nutrition")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Nutrition", description = "Nutrition log management")
public class NutritionController {

    private final NutritionService nutritionService;

    @PostMapping
    @Operation(summary = "Add a nutrition log entry")
    public ResponseEntity<ApiResponse<NutritionLogResponse>> add(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NutritionLogRequest request) {
        log.info("POST /nutrition - user: {}", userDetails.getUsername());
        NutritionLogResponse response = nutritionService.addEntry(
                userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Nutrition entry added"));
    }

    @GetMapping
    @Operation(summary = "Get paginated nutrition logs")
    public ResponseEntity<ApiResponse<Page<NutritionLogResponse>>> getAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NutritionLogResponse> response = nutritionService.getLogs(
                userDetails.getUsername(), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/daily")
    @Operation(summary = "Get all nutrition entries for a specific date")
    public ResponseEntity<ApiResponse<List<NutritionLogResponse>>> getByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.debug("GET /nutrition/daily?date={} - user: {}", date, userDetails.getUsername());
        List<NutritionLogResponse> response = nutritionService.getLogsByDate(
                userDetails.getUsername(), date);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a nutrition entry")
    public ResponseEntity<ApiResponse<NutritionLogResponse>> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id,
            @Valid @RequestBody NutritionLogRequest request) {
        log.info("PUT /nutrition/{} - user: {}", id, userDetails.getUsername());
        NutritionLogResponse response = nutritionService.updateEntry(
                userDetails.getUsername(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Entry updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a nutrition entry")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id) {
        log.info("DELETE /nutrition/{} - user: {}", id, userDetails.getUsername());
        nutritionService.deleteEntry(userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.message("Entry deleted"));
    }
}
