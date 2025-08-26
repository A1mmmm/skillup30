# SkillUp30 - Система геймификации обучения

Полнофункциональное веб-приложение для создания и участия в челленджах с элементами геймификации.

## 🚀 Быстрый старт с Docker

### Полный деплой (Backend + Frontend + PostgreSQL)

```bash
# Клонируйте репозиторий
git clone <repository-url>
cd skillup30

# Запустите все сервисы
docker-compose up -d

# Проверьте статус
docker-compose ps
```

### Только Backend + PostgreSQL

```bash
# Запустите только backend и базу данных
docker-compose up -d postgres backend
```

## 🌐 Доступные URL

После запуска приложение будет доступно по адресам:

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8081
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **PostgreSQL:** localhost:5432

## 🛠 Локальная разработка

### Backend (Spring Boot)

#### Вариант 1: Тестовый режим (H2 база данных)
```bash
cd backend
mvn spring-boot:run "-Dspring-boot.run.profiles=test"
```

#### Вариант 2: Production режим (PostgreSQL)
```bash
# Запустите PostgreSQL
docker-compose up -d postgres

# Запустите backend
cd backend
mvn spring-boot:run
```

### Frontend (React)
```bash
cd frontend
npm install
npm start
```

## 📁 Структура проекта

```
skillup30/
├── backend/                 # Spring Boot приложение
│   ├── src/main/java/
│   │   └── com/skillup30/
│   │       ├── config/      # Конфигурации
│   │       ├── controller/  # REST контроллеры
│   │       ├── dto/         # Data Transfer Objects
│   │       ├── entity/      # JPA сущности
│   │       ├── repository/  # JPA репозитории
│   │       ├── security/    # JWT аутентификация
│   │       └── service/     # Бизнес-логика
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── application-test.properties
│   │   └── db/migration/    # Flyway миграции
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # React приложение
│   ├── src/
│   ├── public/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml       # Docker Compose конфигурация
├── .dockerignore
└── README.md
```

## 🔧 Конфигурация

### Профили Spring Boot

- **test** - H2 in-memory база данных (для разработки)
- **default** - PostgreSQL (production)

### Переменные окружения

#### Backend (.env)
```env
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/skillup30
DB_USERNAME=postgres
DB_PASSWORD=142536

# JWT Configuration
JWT_SECRET=your_secret_key
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Server Configuration
SERVER_PORT=8081
```

#### Frontend (.env)
```env
# API Configuration
REACT_APP_API_URL=http://localhost:8081/api

# Environment
REACT_APP_ENV=development
```

## 🌐 API Endpoints

### Аутентификация
- `POST /api/auth/register` - Регистрация пользователя
- `POST /api/auth/login` - Вход в систему
- `POST /api/auth/refresh` - Обновление токена

### Челленджи
- `GET /api/challenges` - Список всех челленджей
- `GET /api/challenges/{id}` - Детали челленджа
- `POST /api/challenges/{id}/join` - Присоединиться к челленджу
- `GET /api/challenges/my` - Мои челленджи

### Чек-ины
- `POST /api/checkin` - Отметить выполнение дня
- `GET /api/checkin/history` - История чек-инов

### Достижения
- `GET /api/achievements` - Список достижений
- `GET /api/achievements/my` - Мои достижения

## 🛠 Технологии

### Backend
- **Spring Boot 3.2.6**
- **Spring Security** с JWT
- **Spring Data JPA**
- **PostgreSQL** / **H2**
- **Flyway** для миграций
- **Swagger/OpenAPI** для документации

### Frontend
- **React 18**
- **TypeScript**
- **Axios** для API запросов
- **React Router** для навигации
- **Tailwind CSS** для стилизации

### DevOps
- **Docker** для контейнеризации
- **Docker Compose** для оркестрации
- **Nginx** для раздачи статики

## 🚀 Развертывание

### Production с Docker

1. **Клонируйте репозиторий:**
   ```bash
   git clone <repository-url>
   cd skillup30
   ```

2. **Настройте переменные окружения:**
   ```bash
   cp backend/.env.example backend/.env
   cp frontend/.env.example frontend/.env
   # Отредактируйте файлы .env
   ```

3. **Запустите приложение:**
   ```bash
   docker-compose up -d
   ```

4. **Проверьте статус:**
   ```bash
   docker-compose ps
   docker-compose logs -f
   ```

### Локальная разработка

1. **Установите зависимости:**
   - Java 17+
   - Maven
   - Node.js 18+
   - Docker (для PostgreSQL)

2. **Запустите базу данных:**
   ```bash
   docker-compose up -d postgres
   ```

3. **Запустите backend:**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. **Запустите frontend:**
   ```bash
   cd frontend
   npm install
   npm start
   ```

## 🔒 Безопасность

- JWT аутентификация
- CORS настройки
- Валидация входных данных
- Хеширование паролей
- Security headers в nginx

## 📝 Миграции

Миграции автоматически выполняются при запуске приложения через Flyway.

## 🐛 Отладка

### Просмотр логов
```bash
# Все сервисы
docker-compose logs -f

# Только backend
docker-compose logs -f backend

# Только frontend
docker-compose logs -f frontend
```

### Подключение к базе данных
```bash
# Через Docker
docker-compose exec postgres psql -U postgres -d skillup30

# Локально (если PostgreSQL установлен)
psql -h localhost -U postgres -d skillup30
```

## 📄 Лицензия

MIT License 