package com.skillup30.service;

import com.skillup30.dto.AchievementDto;
import com.skillup30.exception.UserNotFoundException;
import com.skillup30.entity.User;
import com.skillup30.entity.UserAchievement;
import com.skillup30.repository.AchievementRepository;
import com.skillup30.repository.UserAchievementRepository;
import com.skillup30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;

    public List<AchievementDto> getUserAchievements(String username) {
        System.out.println("🔍 Getting achievements for user: " + username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        System.out.println("✅ User found: " + user.getUsername() + " (ID: " + user.getId() + ")");
        
        List<UserAchievement> userAchievements = userAchievementRepository.findByUser(user);
        System.out.println("📊 Found " + userAchievements.size() + " user achievements");
        
        List<AchievementDto> result = userAchievements.stream().map(ua -> {
            AchievementDto dto = new AchievementDto();
            dto.setId(ua.getAchievement().getId());
            dto.setName(ua.getAchievement().getName());
            dto.setDescription(ua.getAchievement().getDescription());
            dto.setBadgeImage(ua.getAchievement().getBadgeImage());
            dto.setAwardedAt(ua.getAwardedAt() != null ? ua.getAwardedAt().toString() : null);
            
            System.out.println("🏆 Achievement: " + dto.getName() + " (ID: " + dto.getId() + ") awarded at: " + dto.getAwardedAt());
            return dto;
        }).collect(Collectors.toList());
        
        System.out.println("✅ Returning " + result.size() + " achievements for user " + username);
        return result;
    }

    public List<AchievementDto> getAllAchievements() {
        System.out.println("🔍 Getting all achievements");
        
        List<AchievementDto> result = achievementRepository.findAll().stream().map(a -> {
            AchievementDto dto = new AchievementDto();
            dto.setId(a.getId());
            dto.setName(a.getName());
            dto.setDescription(a.getDescription());
            dto.setBadgeImage(a.getBadgeImage());
            return dto;
        }).collect(Collectors.toList());
        
        System.out.println("📊 Found " + result.size() + " total achievements");
        result.forEach(achievement -> {
            System.out.println("  - " + achievement.getName() + " (ID: " + achievement.getId() + ")");
        });
        
        return result;
    }
}