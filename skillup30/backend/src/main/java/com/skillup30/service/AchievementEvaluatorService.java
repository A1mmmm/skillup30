package com.skillup30.service;

import com.skillup30.entity.*;
import com.skillup30.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementEvaluatorService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final DailyCheckinRepository dailyCheckinRepository;

    @Transactional
    public void evaluateAchievements(User user) {
        System.out.println("🏆 Evaluating achievements for user: " + user.getUsername() + " (ID: " + user.getId() + ")");
        
        List<UserAchievement> userAchievements = userAchievementRepository.findByUser(user);
        List<Long> received = userAchievements.stream().map(ua -> ua.getAchievement().getId()).toList();
        List<Achievement> all = achievementRepository.findAll();
        
        System.out.println("📊 User has " + userAchievements.size() + " achievements, total available: " + all.size());
        
        for (Achievement a : all) {
            if (received.contains(a.getId())) {
                System.out.println("  ✓ " + a.getName() + " - already received");
                continue;
            }
            
            System.out.println("🔍 Checking achievement: " + a.getName() + " (ID: " + a.getId() + ")");
            if (shouldAward(user, a)) {
                System.out.println("🎉 Awarding achievement: " + a.getName());
                UserAchievement ua = new UserAchievement();
                ua.setUser(user);
                ua.setAchievement(a);
                ua.setAwardedAt(java.time.LocalDateTime.now());
                userAchievementRepository.save(ua);
                System.out.println("✅ Achievement saved: " + a.getName());
            } else {
                System.out.println("  ❌ " + a.getName() + " - conditions not met");
            }
        }
        
        System.out.println("🏆 Achievement evaluation completed for user: " + user.getUsername());
    }

    private boolean shouldAward(User user, Achievement a) {
        String name = a.getName().toLowerCase();
        System.out.println("    🔍 Checking conditions for: " + a.getName());
        
        if (name.contains("первый шаг")) {
            long totalCheckins = dailyCheckinRepository.count();
            boolean hasUserCheckins = userChallengeRepository.findByUser(user).stream()
                    .anyMatch(uc -> dailyCheckinRepository.findByUserChallenge(uc).size() > 0);
            boolean result = totalCheckins > 0 && hasUserCheckins;
            System.out.println("      📊 Первый шаг: total checkins=" + totalCheckins + ", has user checkins=" + hasUserCheckins + " -> " + result);
            return result;
        }
        
        if (name.contains("неделя без пропусков")) {
            boolean hasWeek = userChallengeRepository.findByUser(user).stream()
                    .anyMatch(uc -> uc.getCompletedDays() >= 7);
            System.out.println("      📊 Неделя без пропусков: has 7+ days=" + hasWeek);
            return hasWeek;
        }
        
        if (name.contains("месяц без пропусков")) {
            boolean hasMonth = userChallengeRepository.findByUser(user).stream()
                    .anyMatch(uc -> uc.getCompletedDays() >= 30);
            System.out.println("      📊 Месяц без пропусков: has 30+ days=" + hasMonth);
            return hasMonth;
        }
        
        if (name.contains("мульти-челленджер")) {
            long activeChallenges = userChallengeRepository.findByUser(user).stream()
                    .filter(uc -> !uc.isCompleted()).count();
            boolean result = activeChallenges >= 3;
            System.out.println("      📊 Мульти-челленджер: active challenges=" + activeChallenges + " -> " + result);
            return result;
        }
        
        if (name.contains("завершён сложный челлендж")) {
            boolean hasHardCompleted = userChallengeRepository.findByUser(user).stream()
                    .anyMatch(uc -> uc.isCompleted() && uc.getChallenge().getDifficultyLevel().equalsIgnoreCase("hard"));
            System.out.println("      📊 Завершён сложный челлендж: has hard completed=" + hasHardCompleted);
            return hasHardCompleted;
        }
        
        if (name.contains("10 челленджей завершено")) {
            long completedCount = userChallengeRepository.findByUser(user).stream()
                    .filter(UserChallenge::isCompleted).count();
            boolean result = completedCount >= 10;
            System.out.println("      📊 10 челленджей завершено: completed=" + completedCount + " -> " + result);
            return result;
        }
        
        if (name.contains("xp-охотник")) {
            boolean result = user.getXp() >= 1000;
            System.out.println("      📊 XP-охотник: user XP=" + user.getXp() + " -> " + result);
            return result;
        }
        
        if (name.contains("зож-герой")) {
            List<Long> completedIds = userChallengeRepository.findByUser(user).stream()
                    .filter(UserChallenge::isCompleted)
                    .map(uc -> uc.getChallenge().getId())
                    .toList();
            boolean result = completedIds.containsAll(List.of(1L, 7L, 8L));
            System.out.println("      📊 ЗОЖ-герой: completed challenge IDs=" + completedIds + " -> " + result);
            return result;
        }
        
        if (name.contains("творец")) {
            boolean result = userChallengeRepository.findByUser(user).stream()
                    .anyMatch(uc -> uc.getChallenge().getId() == 12L && uc.getCompletedDays() >= 30);
            System.out.println("      📊 Творец: challenge 12 with 30+ days=" + result);
            return result;
        }
        
        System.out.println("      ❌ No matching achievement type found");
        return false;
    }
}