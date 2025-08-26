package com.skillup30.repository;

import com.skillup30.entity.UserAchievement;
import com.skillup30.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser(User user);
}