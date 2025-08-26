package com.skillup30.service;

import com.skillup30.dto.ChallengeDto;
import com.skillup30.dto.ChallengeDayDto;
import com.skillup30.dto.UserChallengeDto;
import com.skillup30.entity.Challenge;
import com.skillup30.entity.ChallengeDay;
import com.skillup30.entity.User;
import com.skillup30.entity.UserChallenge;
import com.skillup30.entity.DailyCheckin;
import com.skillup30.exception.ChallengeNotFoundException;
import com.skillup30.exception.UserNotFoundException;
import com.skillup30.exception.AlreadyJoinedChallengeException;
import com.skillup30.exception.AlreadyCheckedInException;
import com.skillup30.exception.UserNotJoinedChallengeException;
import com.skillup30.repository.ChallengeDayRepository;
import com.skillup30.repository.ChallengeRepository;
import com.skillup30.repository.UserChallengeRepository;
import com.skillup30.repository.UserRepository;
import com.skillup30.repository.DailyCheckinRepository;
import com.skillup30.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeDayRepository challengeDayRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final CheckInService checkInService;

    public List<ChallengeDto> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        System.out.println("🔍 Found " + challenges.size() + " challenges in database");

        challenges.forEach(challenge -> {
            System.out.println("  - ID: " + challenge.getId() + ", Title: " + challenge.getTitle());
        });

        return challenges.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ChallengeDto getChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException(id));

        // Логируем для отладки
        System.out.println("🔍 Getting challenge ID: " + id + ", Title: " + challenge.getTitle());

        return toDto(challenge);
    }

    @Transactional
    public void joinChallenge(String username, Long challengeId) {
        System.out.println(
                "🔍 ChallengeService.joinChallenge called for user '" + username + "' in challenge ID: " + challengeId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        System.out.println("✅ User found: " + user.getUsername() + " (ID: " + user.getId() + ")");

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
        System.out.println("✅ Challenge found: " + challenge.getTitle() + " (ID: " + challenge.getId() + ")");

        var existingUserChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge);
        if (existingUserChallenge.isPresent()) {
            System.out.println("⚠️ User already joined this challenge");
            System.out.println("📊 Existing UserChallenge ID: " + existingUserChallenge.get().getId());
            System.out.println("📊 Join date: " + existingUserChallenge.get().getJoinDate());
            System.out.println("📊 Completed days: " + existingUserChallenge.get().getCompletedDays());
            throw new AlreadyJoinedChallengeException(username, challengeId);
        }

        System.out.println("✅ User has not joined this challenge yet, proceeding with join");

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setUser(user);
        userChallenge.setChallenge(challenge);
        userChallenge.setJoinDate(LocalDateTime.now().toLocalDate());
        userChallenge.setCompletedDays(0);
        userChallenge.setCompleted(false);

        System.out.println("💾 Saving new UserChallenge...");
        UserChallenge savedUserChallenge = userChallengeRepository.save(userChallenge);
        System.out.println("✅ UserChallenge saved with ID: " + savedUserChallenge.getId());

        System.out.println("✅ User '" + username + "' successfully joined challenge '" + challenge.getTitle() + "'");
        System.out.println("📊 Total user challenges after join: " + userChallengeRepository.count());
    }

    public List<ChallengeDto> getActiveChallenges(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<UserChallenge> active = userChallengeRepository.findByUserAndIsCompletedFalse(user);
        return active.stream().map(uc -> toDto(uc.getChallenge())).collect(Collectors.toList());
    }

    public List<ChallengeDayDto> getChallengePlan(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
        List<ChallengeDay> days = challengeDayRepository.findByChallengeOrderByDayNumberAsc(challenge);

        // Логируем для отладки
        System.out.println("🔍 Challenge ID: " + challengeId + ", Title: " + challenge.getTitle());
        System.out.println("📅 Found " + days.size() + " challenge days");

        if (days.isEmpty()) {
            System.out.println("⚠️ No challenge days found for challenge ID: " + challengeId);
        }

        return days.stream().map(day -> {
            ChallengeDayDto dto = new ChallengeDayDto();
            dto.setDayNumber(day.getDayNumber());
            dto.setTask(day.getTask());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void checkIn(String username, Long challengeId) {
        System.out.println(
                "🔍 ChallengeService.checkIn called for user '" + username + "' in challenge ID: " + challengeId);

        // Используем CheckInService для корректной обработки check-in и достижений
        checkInService.checkIn(username, challengeId);

        System.out.println("✅ ChallengeService.checkIn completed successfully for user '" + username
                + "' in challenge ID: " + challengeId);
    }

    public UserChallengeDto getUserProgress(String username, Long challengeId) {
        System.out.println("🔍 ChallengeService.getUserProgress called for user '" + username + "' in challenge ID: "
                + challengeId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        System.out.println("✅ User found: " + user.getUsername() + " (ID: " + user.getId() + ")");

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
        System.out.println("✅ Challenge found: " + challenge.getTitle() + " (ID: " + challenge.getId() + ")");

        // Ищем участие пользователя в челлендже
        var userChallengeOpt = userChallengeRepository.findByUserAndChallenge(user, challenge);
        System.out.println(
                "🔍 Looking for user participation: user ID " + user.getId() + ", challenge ID " + challenge.getId());

        if (userChallengeOpt.isEmpty()) {
            System.out.println(
                    "ℹ️ User '" + username + "' is not participating in challenge '" + challenge.getTitle() + "'");
            System.out.println("📊 Total user challenges in database: " + userChallengeRepository.count());
            System.out.println("📊 Total challenges in database: " + challengeRepository.count());
            System.out.println("📊 Total users in database: " + userRepository.count());
            throw new UserNotJoinedChallengeException(username, challengeId);
        }

        UserChallenge userChallenge = userChallengeOpt.get();
        System.out.println("✅ UserChallenge found: " + userChallenge.getId() + ", completed days: "
                + userChallenge.getCompletedDays());

        return toUserChallengeDto(userChallenge);
    }

    private ChallengeDto toDto(Challenge challenge) {
        ChallengeDto dto = new ChallengeDto();
        dto.setId(challenge.getId());
        dto.setTitle(challenge.getTitle());
        dto.setDescription(challenge.getDescription());
        dto.setRewardPoints(challenge.getRewardPoints());
        dto.setDifficultyLevel(challenge.getDifficultyLevel());
        dto.setCoverImage(challenge.getCoverImage());
        dto.setDailyTask(challenge.getDailyTask());
        return dto;
    }

    private UserChallengeDto toUserChallengeDto(UserChallenge userChallenge) {
        UserChallengeDto dto = new UserChallengeDto();
        dto.setId(userChallenge.getId());
        dto.setChallenge(toDto(userChallenge.getChallenge()));
        dto.setCompletedDays(userChallenge.getCompletedDays());
        dto.setCompleted(userChallenge.isCompleted());
        dto.setStartedAt(userChallenge.getJoinDate().atStartOfDay());
        // completedAt не доступен в текущей структуре UserChallenge
        return dto;
    }
}