package com.skillup30.config;

import com.skillup30.entity.Achievement;
import com.skillup30.entity.Challenge;
import com.skillup30.entity.ChallengeDay;
import com.skillup30.repository.AchievementRepository;
import com.skillup30.repository.ChallengeRepository;
import com.skillup30.repository.ChallengeDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private ChallengeDayRepository challengeDayRepository;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже данные
        if (challengeRepository.count() > 0) {
            return; // Данные уже загружены
        }

        // Создаем челленджи
        List<Challenge> challenges = Arrays.asList(
                createChallenge("30 дней спорта",
                        "Ежедневно занимайтесь любой физической активностью не менее 20 минут. Можно бегать, ходить, заниматься дома или в зале.",
                        120, "medium",
                        "Выполните любую физическую активность не менее 20 минут (бег, ходьба, тренировка дома/в зале)."),

                createChallenge("Чтение книг",
                        "Читайте не менее 15 страниц любой книги каждый день. Развивайте мышление и словарный запас.",
                        90, "easy",
                        "Прочитайте не менее 15 страниц книги. Можно читать художественную, научную или профессиональную литературу."),

                createChallenge("Медитация и дневник",
                        "Медитируйте 10 минут и записывайте 3 вещи, за которые благодарны, каждый день.",
                        100, "medium",
                        "Медитируйте 10 минут и запишите 3 вещи, за которые благодарны, в дневник."),

                createChallenge("Ранний подъём",
                        "Вставайте до 6:30 утра, отмечайте успех и фиксируйте утренние привычки.",
                        150, "hard",
                        "Встаньте до 6:30 утра и отметьте, какие утренние привычки вы выполнили."),

                createChallenge("Изучение английского",
                        "Учите 10 новых слов, смотрите видео или делайте упражнения на английском.",
                        110, "medium",
                        "Выучите 10 новых слов, выполните упражнение или посмотрите видео на английском."),

                createChallenge("Детокс соцсетей",
                        "Не используйте соцсети после 20:00. Освободите время для себя.",
                        80, "easy",
                        "Не используйте соцсети после 20:00. Займитесь чем-то полезным для себя."),

                createChallenge("Питьевой режим",
                        "Выпивайте 1.5–2 литра воды в день. Отмечайте каждый успешный день.",
                        70, "easy",
                        "Выпейте не менее 1.5–2 литров воды за день. Отметьте каждый успешный день."),

                createChallenge("ЗОЖ-питание",
                        "Не ешьте фастфуд, готовьте дома, следите за калориями.",
                        130, "hard",
                        "Не ешьте фастфуд, приготовьте домашнюю еду, следите за калориями."),

                createChallenge("Кодинг марафон",
                        "Пишите код минимум 1 час в день. Любой язык, любой проект.",
                        140, "hard",
                        "Потратьте минимум 1 час на программирование (любой проект, язык, задача)."),

                createChallenge("Финансовый дневник",
                        "Ведите учёт расходов, не тратьте на импульсивные покупки.",
                        100, "medium",
                        "Запишите все расходы за день, проанализируйте траты, избегайте импульсивных покупок."),

                createChallenge("Экологичные привычки",
                        "Сортируйте мусор, не используйте пластик, экономьте воду.",
                        90, "medium",
                        "Сортируйте мусор, не используйте одноразовый пластик, экономьте воду и электричество."),

                createChallenge("Творческий челлендж",
                        "Рисуйте, пишите, фотографируйте или творите что-то новое каждый день.",
                        100, "medium",
                        "Создайте что-то новое: рисунок, текст, фото, музыку или поделку."));

        // Сохраняем челленджи
        List<Challenge> savedChallenges = challengeRepository.saveAll(challenges);

        // Создаем достижения
        List<Achievement> achievements = Arrays.asList(
                createAchievement("Первый шаг", "Сделайте первый чек-ин в любом челлендже"),
                createAchievement("Неделя без пропусков", "7 дней подряд в одном челлендже"),
                createAchievement("Месяц без пропусков", "30 дней подряд без пропусков"),
                createAchievement("Мульти-челленджер", "Участвуйте одновременно в 3 и более челленджах"),
                createAchievement("Завершён сложный челлендж", "Пройдите челлендж с уровнем hard"),
                createAchievement("10 челленджей завершено", "Завершите 10 разных челленджей"),
                createAchievement("XP-охотник", "Наберите 1000 XP за всё время"),
                createAchievement("ЗОЖ-герой", "Пройдите все челленджи по здоровью (спорт, вода, питание)"),
                createAchievement("Творец", "30 дней подряд занимайтесь творчеством"));

        // Сохраняем достижения
        achievementRepository.saveAll(achievements);

        // Создаем детальные планы для челленджей
        createChallengeDays(savedChallenges);

        System.out.println("✅ Тестовые данные успешно загружены в базу!");
    }

    private Challenge createChallenge(String title, String description, int rewardPoints, String difficultyLevel,
            String dailyTask) {
        Challenge challenge = new Challenge();
        challenge.setTitle(title);
        challenge.setDescription(description);
        challenge.setRewardPoints(rewardPoints);
        challenge.setDifficultyLevel(difficultyLevel);
        challenge.setDailyTask(dailyTask);
        return challenge;
    }

    private Achievement createAchievement(String name, String description) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setDescription(description);
        return achievement;
    }

    private void createChallengeDays(List<Challenge> challenges) {
        // Для спортивного челленджа (первый) - детальный план
        Challenge sportChallenge = challenges.get(0);
        List<ChallengeDay> sportDays = Arrays.asList(
                createChallengeDay(sportChallenge, 1, "10 минут быстрой ходьбы"),
                createChallengeDay(sportChallenge, 2, "15 минут растяжки"),
                createChallengeDay(sportChallenge, 3, "20 приседаний"),
                createChallengeDay(sportChallenge, 4, "10 минут йоги"),
                createChallengeDay(sportChallenge, 5, "15 минут плавания или кардио"),
                createChallengeDay(sportChallenge, 6, "30 отжиманий"),
                createChallengeDay(sportChallenge, 7, "20 минут прогулки на свежем воздухе"),
                createChallengeDay(sportChallenge, 8, "10 минут силовой тренировки"),
                createChallengeDay(sportChallenge, 9, "20 минут танцев или аэробики"),
                createChallengeDay(sportChallenge, 10, "15 минут растяжки"),
                createChallengeDay(sportChallenge, 11, "40 приседаний"),
                createChallengeDay(sportChallenge, 12, "10 минут йоги"),
                createChallengeDay(sportChallenge, 13, "20 минут быстрой ходьбы"),
                createChallengeDay(sportChallenge, 14, "30 отжиманий"),
                createChallengeDay(sportChallenge, 15, "20 минут плавания или кардио"),
                createChallengeDay(sportChallenge, 16, "10 минут силовой тренировки"),
                createChallengeDay(sportChallenge, 17, "15 минут растяжки"),
                createChallengeDay(sportChallenge, 18, "50 приседаний"),
                createChallengeDay(sportChallenge, 19, "10 минут йоги"),
                createChallengeDay(sportChallenge, 20, "25 минут прогулки на свежем воздухе"),
                createChallengeDay(sportChallenge, 21, "40 отжиманий"),
                createChallengeDay(sportChallenge, 22, "20 минут танцев или аэробики"),
                createChallengeDay(sportChallenge, 23, "15 минут растяжки"),
                createChallengeDay(sportChallenge, 24, "60 приседаний"),
                createChallengeDay(sportChallenge, 25, "10 минут йоги"),
                createChallengeDay(sportChallenge, 26, "30 минут быстрой ходьбы"),
                createChallengeDay(sportChallenge, 27, "50 отжиманий"),
                createChallengeDay(sportChallenge, 28, "20 минут плавания или кардио"),
                createChallengeDay(sportChallenge, 29, "10 минут силовой тренировки"),
                createChallengeDay(sportChallenge, 30, "30 минут любимого вида спорта"));
        challengeDayRepository.saveAll(sportDays);

        // Для остальных челленджей - шаблонные задания
        for (int i = 1; i < challenges.size(); i++) {
            Challenge challenge = challenges.get(i);
            for (int day = 1; day <= 30; day++) {
                ChallengeDay challengeDay = createChallengeDay(challenge, day,
                        "Выполните задание дня " + day + " для челленджа \"" + challenge.getTitle() + "\"");
                challengeDayRepository.save(challengeDay);
            }
        }
    }

    private ChallengeDay createChallengeDay(Challenge challenge, int dayNumber, String task) {
        ChallengeDay challengeDay = new ChallengeDay();
        challengeDay.setChallenge(challenge);
        challengeDay.setDayNumber(dayNumber);
        challengeDay.setTask(task);
        return challengeDay;
    }
}