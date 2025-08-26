# Установка Docker и деплой проекта SkillUp30

## 1. Установка Docker Desktop для Windows

### Шаг 1: Скачивание Docker Desktop
1. Перейдите на официальный сайт Docker: https://www.docker.com/products/docker-desktop/
2. Нажмите "Download for Windows"
3. Скачайте установщик Docker Desktop

### Шаг 2: Установка
1. Запустите скачанный установщик
2. Следуйте инструкциям установщика
3. Убедитесь, что опция "Use WSL 2 instead of Hyper-V" включена (рекомендуется)
4. Завершите установку и перезагрузите компьютер

### Шаг 3: Проверка установки
После перезагрузки:
1. Запустите Docker Desktop
2. Дождитесь, пока Docker Engine запустится (значок в трее станет зеленым)
3. Откройте PowerShell и выполните:
   ```powershell
   docker --version
   docker-compose --version
   ```

## 2. Деплой проекта

### Шаг 1: Подготовка
1. Убедитесь, что вы находитесь в директории проекта:
   ```powershell
   cd D:\skillup30Final\skillup30
   ```

### Шаг 2: Сборка и запуск
1. Соберите и запустите все сервисы:
   ```powershell
   docker-compose up --build
   ```

   Или запустите в фоновом режиме:
   ```powershell
   docker-compose up --build -d
   ```

### Шаг 3: Проверка работы
После успешного запуска:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8081
- **PostgreSQL**: localhost:5432

## 3. Полезные команды

### Управление контейнерами
```powershell
# Остановить все сервисы
docker-compose down

# Остановить и удалить volumes (данные БД)
docker-compose down -v

# Просмотр логов
docker-compose logs

# Просмотр логов конкретного сервиса
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres

# Перезапуск сервиса
docker-compose restart backend
```

### Управление образами
```powershell
# Просмотр образов
docker images

# Удаление образов
docker rmi skillup30_backend
docker rmi skillup30_frontend

# Очистка неиспользуемых образов
docker system prune -a
```

## 4. Структура проекта

```
skillup30/
├── docker-compose.yml          # Основной файл для запуска всех сервисов
├── .dockerignore              # Файлы, исключаемые из Docker контекста
├── backend/
│   ├── Dockerfile             # Конфигурация для backend (Spring Boot)
│   ├── pom.xml               # Maven зависимости
│   └── src/                  # Исходный код Java
├── frontend/
│   ├── Dockerfile            # Конфигурация для frontend (React)
│   ├── package.json          # Node.js зависимости
│   ├── nginx.conf           # Конфигурация Nginx
│   └── src/                 # Исходный код React
└── README.md                # Документация проекта
```

## 5. Переменные окружения

### Backend (Spring Boot)
- `SPRING_DATASOURCE_URL`: URL базы данных
- `SPRING_DATASOURCE_USERNAME`: Имя пользователя БД
- `SPRING_DATASOURCE_PASSWORD`: Пароль БД
- `JWT_SECRET`: Секретный ключ для JWT
- `CORS_ALLOWED_ORIGINS`: Разрешенные CORS origins

### Frontend (React)
- `REACT_APP_API_URL`: URL API backend
- `REACT_APP_ENV`: Окружение (production/development)

### PostgreSQL
- `POSTGRES_DB`: Имя базы данных
- `POSTGRES_USER`: Пользователь БД
- `POSTGRES_PASSWORD`: Пароль БД

## 6. Устранение проблем

### Проблема: Порт уже занят
```powershell
# Найти процесс, использующий порт
netstat -ano | findstr :8081
netstat -ano | findstr :3000
netstat -ano | findstr :5432

# Остановить процесс
taskkill /PID <номер_процесса> /F
```

### Проблема: Docker не запускается
1. Убедитесь, что Docker Desktop запущен
2. Проверьте, что WSL 2 включен
3. Перезапустите Docker Desktop

### Проблема: Ошибки сборки
```powershell
# Очистить кэш Docker
docker system prune -a

# Пересобрать образы
docker-compose build --no-cache
```

## 7. Разработка

### Локальная разработка без Docker
1. Запустите PostgreSQL через Docker:
   ```powershell
   docker-compose up postgres -d
   ```

2. Запустите backend локально:
   ```powershell
   cd backend
   mvn spring-boot:run
   ```

3. Запустите frontend локально:
   ```powershell
   cd frontend
   npm start
   ```

### Hot Reload в Docker
Для разработки с hot reload добавьте volumes в docker-compose.yml:
```yaml
backend:
  volumes:
    - ./backend/src:/app/src
  environment:
    SPRING_PROFILES_ACTIVE: dev
```

## 8. Production деплой

Для production окружения рекомендуется:
1. Использовать внешнюю базу данных
2. Настроить SSL/TLS
3. Использовать reverse proxy (Nginx)
4. Настроить мониторинг и логирование
5. Использовать Docker Swarm или Kubernetes для оркестрации 