# Настройка переменных окружения

## Backend (Spring Boot)

### 1. Создайте файл `.env` в папке `backend/`

Скопируйте содержимое из `env.example` и заполните реальными значениями:

```bash
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

### 2. Важные замечания:

- **DB_PASSWORD**: Укажите реальный пароль от вашей PostgreSQL базы данных
- **JWT_SECRET**: Создайте длинный случайный ключ (минимум 32 символа)
- **CORS_ALLOWED_ORIGINS**: В продакшене укажите реальный домен frontend

## Frontend (React)

### 1. Создайте файл `.env` в папке `frontend/`

Скопируйте содержимое из `env.example`:

```bash
# API Configuration
REACT_APP_API_URL=http://localhost:8081/api

# Environment
REACT_APP_ENV=development
```

### 2. Важные замечания:

- В React все переменные окружения должны начинаться с `REACT_APP_`
- После изменения переменных окружения перезапустите приложение

## Безопасность

⚠️ **ВАЖНО**: Никогда не коммитьте файлы `.env` в git!

- Файлы `.env` уже добавлены в `.gitignore`
- Используйте `env.example` как шаблон
- В продакшене используйте переменные окружения сервера

## Запуск приложения

### Backend:
```bash
cd backend
# Убедитесь, что PostgreSQL запущен и база данных создана
mvn spring-boot:run
```

### Frontend:
```bash
cd frontend
npm install
npm start
```

## Проверка конфигурации

После запуска проверьте:
1. Backend доступен по адресу: http://localhost:8081
2. Frontend доступен по адресу: http://localhost:3000
3. API запросы работают корректно 