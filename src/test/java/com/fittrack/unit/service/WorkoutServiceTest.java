package com.fittrack.unit.service;

import com.fittrack.domain.entity.User;
import com.fittrack.domain.entity.WorkoutSession;
import com.fittrack.domain.enums.Role;
import com.fittrack.domain.enums.WorkoutType;
import com.fittrack.dto.request.WorkoutSessionRequest;
import com.fittrack.dto.response.WorkoutSessionResponse;
import com.fittrack.exception.ResourceNotFoundException;
import com.fittrack.mapper.WorkoutMapper;
import com.fittrack.repository.WorkoutSessionRepository;
import com.fittrack.service.UserService;
import com.fittrack.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkoutService Unit Tests")
class WorkoutServiceTest {

    @Mock private WorkoutSessionRepository workoutRepository;
    @Mock private WorkoutMapper workoutMapper;
    @Mock private UserService userService;

    @InjectMocks
    private WorkoutService workoutService;

    private User testUser;
    private WorkoutSession testSession;
    private WorkoutSessionResponse testResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@fittrack.com")
                .role(Role.USER)
                .isActive(true)
                .streakDays(0)
                .build();

        testSession = WorkoutSession.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .name("Morning Strength")
                .workoutType(WorkoutType.STRENGTH)
                .startedAt(LocalDateTime.now().minusHours(1))
                .endedAt(LocalDateTime.now())
                .durationMinutes(60)
                .caloriesBurned(450)
                .build();

        testResponse = WorkoutSessionResponse.builder()
                .id(testSession.getId())
                .name("Morning Strength")
                .workoutType(WorkoutType.STRENGTH)
                .durationMinutes(60)
                .caloriesBurned(450)
                .startedAt(testSession.getStartedAt())
                .build();
    }

    @Test
    @DisplayName("createWorkout: success — session saved and response returned")
    void createWorkout_success() {
        WorkoutSessionRequest request = new WorkoutSessionRequest();
        request.setName("Morning Strength");
        request.setWorkoutType(WorkoutType.STRENGTH);
        request.setStartedAt(LocalDateTime.now().minusHours(1));
        request.setDurationMinutes(60);
        request.setCaloriesBurned(450);

        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(workoutMapper.toEntity(any(WorkoutSessionRequest.class))).thenReturn(testSession);
        when(workoutRepository.save(any(WorkoutSession.class))).thenReturn(testSession);
        when(workoutMapper.toResponse(any(WorkoutSession.class))).thenReturn(testResponse);

        WorkoutSessionResponse result = workoutService.createWorkout("test@fittrack.com", request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Morning Strength");
        assertThat(result.getDurationMinutes()).isEqualTo(60);
        verify(workoutRepository).save(any(WorkoutSession.class));
    }

    @Test
    @DisplayName("getWorkouts: returns paginated results for user")
    void getWorkouts_returnsPaginatedResults() {
        Page<WorkoutSession> sessionPage = new PageImpl<>(List.of(testSession));
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(workoutRepository.findByUserIdOrderByStartedAtDesc(any(UUID.class), any(Pageable.class)))
                .thenReturn(sessionPage);
        when(workoutMapper.toResponse(any(WorkoutSession.class))).thenReturn(testResponse);

        Page<WorkoutSessionResponse> result = workoutService.getWorkouts("test@fittrack.com", 0, 20);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Morning Strength");
    }

    @Test
    @DisplayName("getWorkoutById: throws ResourceNotFoundException when not found")
    void getWorkoutById_notFound_throws() {
        UUID workoutId = UUID.randomUUID();
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(workoutRepository.findByIdAndUserId(any(UUID.class), any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.getWorkoutById("test@fittrack.com", workoutId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(workoutId.toString());
    }

    @Test
    @DisplayName("deleteWorkout: success — repository deleteById called")
    void deleteWorkout_success() {
        UUID workoutId = testSession.getId();
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(workoutRepository.existsByIdAndUserId(any(UUID.class), any(UUID.class))).thenReturn(true);

        workoutService.deleteWorkout("test@fittrack.com", workoutId);

        verify(workoutRepository).deleteById(workoutId);
    }

    @Test
    @DisplayName("deleteWorkout: throws ResourceNotFoundException when not found")
    void deleteWorkout_notFound_throws() {
        UUID workoutId = UUID.randomUUID();
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(workoutRepository.existsByIdAndUserId(any(UUID.class), any(UUID.class))).thenReturn(false);

        assertThatThrownBy(() -> workoutService.deleteWorkout("test@fittrack.com", workoutId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(workoutRepository, never()).deleteById(any());
    }
}
