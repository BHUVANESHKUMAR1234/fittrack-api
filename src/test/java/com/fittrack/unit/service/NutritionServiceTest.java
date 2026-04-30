package com.fittrack.unit.service;

import com.fittrack.domain.entity.NutritionLog;
import com.fittrack.domain.entity.User;
import com.fittrack.domain.enums.MealType;
import com.fittrack.domain.enums.Role;
import com.fittrack.dto.request.NutritionLogRequest;
import com.fittrack.dto.response.NutritionLogResponse;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.mapper.NutritionMapper;
import com.fittrack.repository.NutritionLogRepository;
import com.fittrack.service.NutritionService;
import com.fittrack.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NutritionService Unit Tests")
class NutritionServiceTest {

    @Mock private NutritionLogRepository nutritionRepository;
    @Mock private NutritionMapper nutritionMapper;
    @Mock private UserService userService;

    @InjectMocks
    private NutritionService nutritionService;

    private User testUser;
    private NutritionLog testLog;
    private NutritionLogResponse testResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@fittrack.com")
                .role(Role.USER)
                .isActive(true)
                .streakDays(0)
                .build();

        testLog = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .logDate(LocalDate.now())
                .mealType(MealType.BREAKFAST)
                .foodName("Oats with Banana")
                .quantityGrams(300.0)
                .calories(420)
                .proteinG(12.0)
                .carbsG(75.0)
                .fatG(6.0)
                .build();

        testResponse = NutritionLogResponse.builder()
                .id(testLog.getId())
                .logDate(LocalDate.now())
                .mealType(MealType.BREAKFAST)
                .foodName("Oats with Banana")
                .calories(420)
                .build();
    }

    @Test
    @DisplayName("addEntry: success — log saved and response returned")
    void addEntry_success() {
        NutritionLogRequest request = new NutritionLogRequest();
        request.setLogDate(LocalDate.now());
        request.setMealType(MealType.BREAKFAST);
        request.setFoodName("Oats with Banana");
        request.setQuantityGrams(300.0);
        request.setCalories(420);

        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(nutritionMapper.toEntity(any())).thenReturn(testLog);
        when(nutritionRepository.save(any())).thenReturn(testLog);
        when(nutritionMapper.toResponse(any())).thenReturn(testResponse);

        NutritionLogResponse result = nutritionService.addEntry("test@fittrack.com", request);

        assertThat(result).isNotNull();
        assertThat(result.getFoodName()).isEqualTo("Oats with Banana");
        assertThat(result.getCalories()).isEqualTo(420);
        verify(nutritionRepository).save(any());
    }

    @Test
    @DisplayName("getLogsByDate: returns entries for specific date")
    void getLogsByDate_returnsEntries() {
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(nutritionRepository.findByUserIdAndLogDateOrderByMealType(any(), any()))
                .thenReturn(List.of(testLog));
        when(nutritionMapper.toResponse(any())).thenReturn(testResponse);

        List<NutritionLogResponse> result = nutritionService
                .getLogsByDate("test@fittrack.com", LocalDate.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMealType()).isEqualTo(MealType.BREAKFAST);
    }

    @Test
    @DisplayName("deleteEntry: throws ResourceNotFoundException when entry not found")
    void deleteEntry_notFound_throws() {
        UUID entryId = UUID.randomUUID();
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(nutritionRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutritionService.deleteEntry("test@fittrack.com", entryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(entryId.toString());

        verify(nutritionRepository, never()).delete(any());
    }
}
