package com.fittrack.repository;

import com.fittrack.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.streakDays = u.streakDays + 1 WHERE u.id = :id")
    void incrementStreak(UUID id);

    @Modifying
    @Query("UPDATE User u SET u.streakDays = 0 WHERE u.id = :id")
    void resetStreak(UUID id);
}
