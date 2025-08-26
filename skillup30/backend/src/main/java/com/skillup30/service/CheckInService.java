package com.skillup30.service;

import com.skillup30.dto.CheckInResponse;
import com.skillup30.exception.UserNotFoundException;
import com.skillup30.exception.ChallengeNotFoundException;
import com.skillup30.exception.UserNotJoinedChallengeException;
import com.skillup30.entity.*;
import com.skillup30.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final AchievementEvaluatorService achievementEvaluatorService;

    @Transactional
    public CheckInResponse checkIn(String username, Long challengeId) {
        System.out.println("🔍 CheckInService.checkIn called for user: " + username + ", challenge: " + challengeId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
        UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge)
                .orElseThrow(() -> new UserNotJoinedChallengeException(username, challengeId));
        if (userChallenge.isCompleted()) {
            return new CheckInResponse(false, "Челлендж уже завершён", userChallenge.getCompletedDays(), true);
        }
        LocalDate today = LocalDate.now();
        if (dailyCheckinRepository.findByUserChallengeAndDate(userChallenge, today).isPresent()) {
            return new CheckInResponse(false, "Чек-ин за сегодня уже был", userChallenge.getCompletedDays(), false);
        }
        DailyCheckin checkin = new DailyCheckin();
        checkin.setUserChallenge(userChallenge);
        checkin.setDate(today);
        dailyCheckinRepository.save(checkin);
        userChallenge.setCompletedDays(userChallenge.getCompletedDays() + 1);
        int xpForCheckIn = switch (challenge.getDifficultyLevel().toLowerCase()) {
            case "hard" -> 15;
            case "medium" -> 12;
            default -> 10;
        };
        user.setXp(user.getXp() + xpForCheckIn);
        int newLevel = 1 + user.getXp() / 100;
        if (newLevel > user.getLevel())
            user.setLevel(newLevel);
        boolean completedNow = false;
        if (userChallenge.getCompletedDays() >= 30) {
            userChallenge.setCompleted(true);
            user.setXp(user.getXp() + challenge.getRewardPoints());
            user.setTotalPoints(user.getTotalPoints() + challenge.getRewardPoints());
            completedNow = true;
        }
        userRepository.save(user);
        userChallengeRepository.save(userChallenge);

        System.out.println("🏆 Starting achievement evaluation for user: " + user.getUsername());
        achievementEvaluatorService.evaluateAchievements(user);
        System.out.println("✅ Achievement evaluation completed for user: " + user.getUsername());

        return new CheckInResponse(true, completedNow ? "Челлендж завершён!" : "Чек-ин успешно выполнен",
                userChallenge.getCompletedDays(), userChallenge.isCompleted());
    }
}