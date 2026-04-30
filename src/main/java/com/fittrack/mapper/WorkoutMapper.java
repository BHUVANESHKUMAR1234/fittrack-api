package com.fittrack.mapper;

import com.fittrack.domain.entity.ExerciseLog;
import com.fittrack.domain.entity.WorkoutSession;
import com.fittrack.dto.request.ExerciseLogRequest;
import com.fittrack.dto.request.WorkoutSessionRequest;
import com.fittrack.dto.response.ExerciseLogResponse;
import com.fittrack.dto.response.WorkoutSessionResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exerciseLogs", ignore = true)
    WorkoutSession toEntity(WorkoutSessionRequest request);

    @Mapping(source = "exerciseLogs", target = "exercises")
    WorkoutSessionResponse toResponse(WorkoutSession session);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workoutSession", ignore = true)
    ExerciseLog toEntity(ExerciseLogRequest request);

    ExerciseLogResponse toResponse(ExerciseLog exerciseLog);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exerciseLogs", ignore = true)
    void updateFromRequest(WorkoutSessionRequest request, @MappingTarget WorkoutSession session);
}
