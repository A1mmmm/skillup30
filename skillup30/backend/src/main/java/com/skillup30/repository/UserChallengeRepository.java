package com.skillup30.repository;

import com.skillup30.entity.UserChallenge;
import com.skillup30.entity.User;
import com.skillup30.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    List<UserChallenge> findByUser(User user);

    List<UserChallenge> findByUserAndIsCompletedFalse(User user);

    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
}