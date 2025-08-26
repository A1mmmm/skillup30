CREATE TABLE users (
    id bigserial primary key,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    level INT DEFAULT 1,
    xp INT DEFAULT 0,
    total_points INT DEFAULT 0
);

CREATE TABLE challenges (
    id bigserial primary key,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    reward_points INT NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    cover_image VARCHAR(255),
    daily_task TEXT NOT NULL
);

CREATE TABLE user_challenges (
    id bigserial primary key,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    challenge_id BIGINT NOT NULL REFERENCES challenges(id) ON DELETE CASCADE,
    join_date DATE NOT NULL,
    completed_days INT DEFAULT 0,
    is_completed BOOLEAN DEFAULT FALSE,
    UNIQUE(user_id, challenge_id)
);

CREATE TABLE daily_checkins (
    id bigserial primary key,
    user_challenge_id BIGINT NOT NULL REFERENCES user_challenges(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    UNIQUE(user_challenge_id, date)
);

CREATE TABLE achievements (
    id bigserial primary key,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    badge_image VARCHAR(255)
);

CREATE TABLE user_achievements (
    id bigserial primary key,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    achievement_id BIGINT NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    awarded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, achievement_id)
);

CREATE TABLE challenge_days (
    id bigserial primary key,
    challenge_id BIGINT NOT NULL REFERENCES challenges(id) ON DELETE CASCADE,
    day_number INT NOT NULL,
    task TEXT NOT NULL,
    UNIQUE(challenge_id, day_number)
); 