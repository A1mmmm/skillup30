package com.skillup30.service;

import com.skillup30.dto.UserProfileDto;
import com.skillup30.dto.UserProgressDto;
import com.skillup30.dto.UserChallengeDto;
import com.skillup30.exception.UserNotFoundException;
import com.skillup30.entity.User;
import com.skillup30.entity.UserChallenge;
import com.skillup30.repository.UserChallengeRepository;
import com.skillup30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    public UserProfileDto getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setLevel(user.getLevel());
        dto.setXp(user.getXp());
        dto.setTotalPoints(user.getTotalPoints());
        return dto;
    }

    public List<UserProgressDto> getProgress(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<UserChallenge> userChallenges = userChallengeRepository.findByUser(user);
        return userChallenges.stream().map(uc -> {
            UserProgressDto dto = new UserProgressDto();
            dto.setChallengeId(uc.getChallenge().getId());
            dto.setChallengeTitle(uc.getChallenge().getTitle());
            dto.setCompletedDays(uc.getCompletedDays());
            dto.setCompleted(uc.isCompleted());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<UserChallengeDto> getUserChallenges(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<UserChallenge> userChallenges = userChallengeRepository.findByUser(user);
        return userChallenges.stream().map(this::toUserChallengeDto).collect(Collectors.toList());
    }

    public List<UserChallengeDto> getTodayChallenges(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<UserChallenge> userChallenges = userChallengeRepository.findByUserAndIsCompletedFalse(user);
        return userChallenges.stream().map(this::toUserChallengeDto).collect(Collectors.toList());
    }

    private UserChallengeDto toUserChallengeDto(UserChallenge userChallenge) {
        UserChallengeDto dto = new UserChallengeDto();
        dto.setId(userChallenge.getId());
        dto.setChallenge(toChallengeDto(userChallenge.getChallenge()));
        dto.setCompletedDays(userChallenge.getCompletedDays());
        dto.setCompleted(userChallenge.isCompleted());
        dto.setStartedAt(userChallenge.getJoinDate().atStartOfDay());
        return dto;
    }

    private com.skillup30.dto.ChallengeDto toChallengeDto(com.skillup30.entity.Challenge challenge) {
        com.skillup30.dto.ChallengeDto dto = new com.skillup30.dto.ChallengeDto();
        dto.setId(challenge.getId());
        dto.setTitle(challenge.getTitle());
        dto.setDescription(challenge.getDescription());
        dto.setRewardPoints(challenge.getRewardPoints());
        dto.setDifficultyLevel(challenge.getDifficultyLevel());
        dto.setCoverImage(challenge.getCoverImage());
        dto.setDailyTask(challenge.getDailyTask());
        return dto;
    }
}