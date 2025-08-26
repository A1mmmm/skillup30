package com.skillup30.repository;

import com.skillup30.entity.ChallengeDay;
import com.skillup30.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChallengeDayRepository extends JpaRepository<ChallengeDay, Long> {
    List<ChallengeDay> findByChallengeOrderByDayNumberAsc(Challenge challenge);
}