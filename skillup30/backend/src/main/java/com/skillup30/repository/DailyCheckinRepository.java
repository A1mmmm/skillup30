package com.skillup30.repository;

import com.skillup30.entity.DailyCheckin;
import com.skillup30.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyCheckinRepository extends JpaRepository<DailyCheckin, Long> {
    Optional<DailyCheckin> findByUserChallengeAndDate(UserChallenge userChallenge, LocalDate date);

    List<DailyCheckin> findByUserChallenge(UserChallenge userChallenge);
}