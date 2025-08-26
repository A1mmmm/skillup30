# SkillUp30 Backend

Backend приложение для системы 30-дневных челленджей с геймификацией.

## Технологии

- **Spring Boot 3.2.6** - основной фреймворк
- **Java 17** - язык программирования
- **Spring Security** - аутентификация и авторизация
- **JWT** - JSON Web Tokens для аутентификации
- **Spring Data JPA** - работа с базой данных
- **PostgreSQL** - база данных
- **Flyway** - миграции базы данных
- **Swagger/OpenAPI 3** - документация API
- **Lombok** - уменьшение boilerplate кода

## Структура проекта

```
src/main/java/com/skillup30/
├── config/                 # Конфигурации
│   ├── OpenApiConfig.java  # Конфигурация Swagger
│   └── SecurityConfig.java # Конфигурация безопасности
├── controller/             # REST контроллеры
│   ├── AuthController.java
│   ├── ChallengeController.java
│   ├── UserController.java
│   ├── LeaderboardController.java
│   └── AchievementController.java
├── dto/                    # Data Transfer Objects
│   ├── ChallengeDto.java
│   ├── UserChallengeDto.java
│   ├── UserProfileDto.java
│   ├── ErrorResponse.java
│   └── ...
├── entity/                 # JPA сущности
├── repository/             # Репозитории для работы с БД
├── service/                # Бизнес-логика
├── security/               # JWT фильтры и утилиты
└── exception/              # Кастомные исключения
    ├── GlobalExceptionHandler.java
    ├── UserNotFoundException.java
    ├── ChallengeNotFoundException.java
    └── ...
```

## API Документация

После запуска приложения документация Swagger доступна по адресу:
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

### Основные эндпоинты

#### Аутентификация (`/api/auth`)
- `POST /register` - регистрация пользователя
- `POST /login` - вход пользователя
- `POST /refresh` - обновление JWT токена

#### Челленджи (`/api/challenges`)
- `GET /` - получить все челленджи
- `GET /{id}` - получить челлендж по ID
- `POST /{id}/join` - присоединиться к челленджу
- `POST /{id}/checkin` - отметить день
- `GET /{id}/user-progress` - получить прогресс пользователя

#### Пользователи (`/api/users`)
- `GET /me` - получить профиль пользователя
- `GET /me/challenges` - получить челленджи пользователя
- `GET /me/today-challenges` - получить челленджи на сегодня

#### Лидерборд (`/api/leaderboard`)
- `GET /` - получить рейтинг пользователей

#### Достижения (`/api/achievements`)
- `GET /` - получить достижения пользователя
- `GET /all` - получить все достижения

## Конфигурация

### Переменные окружения

Создайте файл `.env` в корне backend директории:

```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/skillup30
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password_here

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here_make_it_long_and_random
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Server Configuration
SERVER_PORT=8081
```

### Запуск

1. Убедитесь, что PostgreSQL запущен и создана база данных `skillup30`
2. Настройте переменные окружения
3. Запустите приложение:
   ```bash
   ./mvnw spring-boot:run
   ```

## Особенности реализации

### Swagger/OpenAPI
- Полная документация всех API эндпоинтов
- Автоматическая генерация схемы API
- Интерактивный интерфейс для тестирования
- Поддержка JWT аутентификации в документации

### Обработка ошибок
- Глобальный обработчик исключений
- Стандартизированные ответы с ошибками
- Валидация входных данных
- Детальные сообщения об ошибках

### Безопасность
- JWT аутентификация
- Хеширование паролей с BCrypt
- CORS конфигурация
- Защищенные эндпоинты

### Валидация
- Аннотации валидации в DTO
- Автоматическая проверка входных данных
- Кастомные сообщения об ошибках

## Интеграция с Frontend

Backend полностью совместим с новой структурой frontend:
- Поддержка всех необходимых эндпоинтов
- Правильная обработка CORS
- Совместимые форматы данных
- JWT аутентификация 