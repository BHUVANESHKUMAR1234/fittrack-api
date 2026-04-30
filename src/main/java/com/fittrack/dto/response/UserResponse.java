package com.fittrack.dto.response;

import com.fittrack.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Double heightCm;
    private Double weightKg;
    private Role role;
    private Integer streakDays;
    private LocalDateTime createdAt;
}
