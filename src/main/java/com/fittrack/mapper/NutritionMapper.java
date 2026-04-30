package com.fittrack.mapper;

import com.fittrack.domain.entity.NutritionLog;
import com.fittrack.dto.request.NutritionLogRequest;
import com.fittrack.dto.response.NutritionLogResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NutritionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    NutritionLog toEntity(NutritionLogRequest request);

    NutritionLogResponse toResponse(NutritionLog nutritionLog);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromRequest(NutritionLogRequest request, @MappingTarget NutritionLog log);
}
