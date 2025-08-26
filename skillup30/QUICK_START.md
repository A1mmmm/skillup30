# 🚀 Быстрый старт проекта SkillUp30

## Предварительные требования

1. **Docker Desktop** - [Скачать и установить](https://www.docker.com/products/docker-desktop/)
2. **PowerShell** (уже установлен в Windows)

## Быстрый запуск

### Вариант 1: Автоматический запуск (рекомендуется)

1. Откройте PowerShell в директории проекта:
   ```powershell
   cd D:\skillup30Final\skillup30
   ```

2. Запустите скрипт автоматического запуска:
   ```powershell
   .\start-docker.ps1
   ```

3. Следуйте инструкциям скрипта

### Вариант 2: Ручной запуск

1. Откройте PowerShell в директории проекта:
   ```powershell
   cd D:\skillup30Final\skillup30
   ```

2. Соберите и запустите проект:
   ```powershell
   docker-compose up --build -d
   ```

3. Проверьте статус:
   ```powershell
   docker-compose ps
   ```

## Доступные сервисы

После успешного запуска:

- 🌐 **Frontend**: http://localhost:3000
- 🔧 **Backend API**: http://localhost:8081
- 🗄️ **PostgreSQL**: localhost:5432

## Остановка проекта

### Автоматическая остановка:
```powershell
.\stop-docker.ps1
```

### Ручная остановка:
```powershell
docker-compose down
```

## Полезные команды

```powershell
# Просмотр логов
docker-compose logs

# Просмотр логов конкретного сервиса
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres

# Перезапуск сервиса
docker-compose restart backend

# Остановка и удаление данных БД
docker-compose down -v

# Очистка Docker кэша
docker system prune -a
```

## Устранение проблем

### Docker не найден
- Установите Docker Desktop: https://www.docker.com/products/docker-desktop/
- Перезагрузите компьютер после установки

### Порт занят
```powershell
# Найти процесс
netstat -ano | findstr :8081
netstat -ano | findstr :3000

# Остановить процесс
taskkill /PID <номер_процесса> /F
```

### Ошибки сборки
```powershell
# Очистить кэш и пересобрать
docker system prune -a
docker-compose build --no-cache
```

## Структура проекта

```
skillup30/
├── docker-compose.yml          # Конфигурация всех сервисов
├── start-docker.ps1           # Скрипт запуска
├── stop-docker.ps1            # Скрипт остановки
├── DOCKER_SETUP.md            # Подробная инструкция
├── env.example                # Пример переменных окружения
├── backend/
│   ├── Dockerfile             # Backend контейнер
│   └── src/                   # Java код
└── frontend/
    ├── Dockerfile             # Frontend контейнер
    └── src/                   # React код
```

## Поддержка

Если у вас возникли проблемы:
1. Проверьте файл `DOCKER_SETUP.md` для подробных инструкций
2. Убедитесь, что Docker Desktop запущен
3. Проверьте логи: `docker-compose logs`

---

**🎉 Готово! Ваш проект SkillUp30 запущен в Docker!** 