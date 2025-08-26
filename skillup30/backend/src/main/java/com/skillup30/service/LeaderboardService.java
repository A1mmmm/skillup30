package com.skillup30.service;

import com.skillup30.dto.LeaderboardEntryDto;
import com.skillup30.entity.User;
import com.skillup30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final UserRepository userRepository;

    public List<LeaderboardEntryDto> getLeaderboard() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> {
                    if (u2.getLevel() != u1.getLevel()) {
                        return Integer.compare(u2.getLevel(), u1.getLevel());
                    } else if (u2.getXp() != u1.getXp()) {
                        return Integer.compare(u2.getXp(), u1.getXp());
                    } else {
                        return Integer.compare(u2.getTotalPoints(), u1.getTotalPoints());
                    }
                })
                .limit(20)
                .map(u -> {
                    LeaderboardEntryDto dto = new LeaderboardEntryDto();
                    dto.setUserId(u.getId());
                    dto.setUsername(u.getUsername());
                    dto.setLevel(u.getLevel());
                    dto.setXp(u.getXp());
                    dto.setTotalPoints(u.getTotalPoints());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}