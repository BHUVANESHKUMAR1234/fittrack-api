package com.fittrack.service;

import com.fittrack.domain.entity.NutritionLog;
import com.fittrack.domain.entity.User;
import com.fittrack.dto.request.NutritionLogRequest;
import com.fittrack.dto.response.NutritionLogResponse;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.mapper.NutritionMapper;
import com.fittrack.repository.NutritionLogRepository;
import com.fittrack.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Business logic for nutrition log management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionLogRepository nutritionRepository;
    private final NutritionMapper nutritionMapper;
    private final UserService userService;

    @Transactional
    public NutritionLogResponse addEntry(String email, NutritionLogRequest request) {
        log.info("Adding nutrition entry for user: {}", email);
        User user = userService.findByEmail(email);

        NutritionLog log = nutritionMapper.toEntity(request);
        log.setUser(user);

        NutritionLog saved = nutritionRepository.save(log);
        return nutritionMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<NutritionLogResponse> getLogs(String email, int page, int size) {
        User user = userService.findByEmail(email);
        int safeSize = Math.min(size, AppConstants.MAX_PAGE_SIZE);
        return nutritionRepository
                .findByUserIdOrderByLogDateDesc(user.getId(), PageRequest.of(page, safeSize))
                .map(nutritionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<NutritionLogResponse> getLogsByDate(String email, LocalDate date) {
        User user = userService.findByEmail(email);
        return nutritionRepository
                .findByUserIdAndLogDateOrderByMealType(user.getId(), date)
                .stream()
                .map(nutritionMapper::toResponse)
                .toList();
    }

    @Transactional
    public NutritionLogResponse updateEntry(String email, UUID entryId, NutritionLogRequest request) {
        log.info("Updating nutrition entry {} for user: {}", entryId, email);
        User user = userService.findByEmail(email);
        NutritionLog entry = nutritionRepository.findByIdAndUserId(entryId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry not found: " + entryId));

        nutritionMapper.updateFromRequest(request, entry);
        NutritionLog saved = nutritionRepository.save(entry);
        return nutritionMapper.toResponse(saved);
    }

    @Transactional
    public void deleteEntry(String email, UUID entryId) {
        log.info("Deleting nutrition entry {} for user: {}", entryId, email);
        User user = userService.findByEmail(email);
        NutritionLog entry = nutritionRepository.findByIdAndUserId(entryId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition entry not found: " + entryId));
        nutritionRepository.delete(entry);
    }
}
