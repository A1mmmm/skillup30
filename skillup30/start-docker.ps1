# Скрипт для запуска проекта SkillUp30 в Docker
# Автор: AI Assistant
# Дата: $(Get-Date -Format "yyyy-MM-dd")

Write-Host "🚀 Запуск проекта SkillUp30 в Docker..." -ForegroundColor Green

# Проверяем, установлен ли Docker
try {
    $dockerVersion = docker --version 2>$null
    if ($dockerVersion) {
        Write-Host "✅ Docker найден: $dockerVersion" -ForegroundColor Green
    } else {
        Write-Host "❌ Docker не найден. Пожалуйста, установите Docker Desktop." -ForegroundColor Red
        Write-Host "📖 Инструкция по установке: DOCKER_SETUP.md" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "❌ Docker не найден. Пожалуйста, установите Docker Desktop." -ForegroundColor Red
    Write-Host "📖 Инструкция по установке: DOCKER_SETUP.md" -ForegroundColor Yellow
    exit 1
}

# Проверяем, запущен ли Docker
try {
    docker info >$null 2>&1
    Write-Host "✅ Docker Engine запущен" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker Engine не запущен. Запустите Docker Desktop." -ForegroundColor Red
    exit 1
}

# Проверяем, находимся ли мы в правильной директории
if (-not (Test-Path "docker-compose.yml")) {
    Write-Host "❌ Файл docker-compose.yml не найден. Убедитесь, что вы находитесь в корневой директории проекта." -ForegroundColor Red
    exit 1
}

Write-Host "📁 Текущая директория: $(Get-Location)" -ForegroundColor Cyan

# Останавливаем существующие контейнеры
Write-Host "🛑 Остановка существующих контейнеров..." -ForegroundColor Yellow
docker-compose down 2>$null

# Очищаем неиспользуемые образы (опционально)
$cleanImages = Read-Host "🧹 Очистить неиспользуемые Docker образы? (y/N)"
if ($cleanImages -eq "y" -or $cleanImages -eq "Y") {
    Write-Host "🧹 Очистка неиспользуемых образов..." -ForegroundColor Yellow
    docker system prune -f
}

# Собираем и запускаем проект
Write-Host "🔨 Сборка и запуск проекта..." -ForegroundColor Yellow
Write-Host "⏳ Это может занять несколько минут при первом запуске..." -ForegroundColor Cyan

# Запускаем в фоновом режиме
docker-compose up --build -d

# Проверяем статус контейнеров
Write-Host "📊 Проверка статуса контейнеров..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

$containers = docker-compose ps
Write-Host $containers -ForegroundColor Cyan

# Проверяем, что все контейнеры запущены
$runningContainers = docker-compose ps --format "table {{.Name}}\t{{.Status}}" | Select-String "Up"
$expectedContainers = 3  # postgres, backend, frontend

if ($runningContainers.Count -ge $expectedContainers) {
    Write-Host "✅ Все сервисы успешно запущены!" -ForegroundColor Green
    Write-Host ""
    Write-Host "🌐 Доступные сервисы:" -ForegroundColor Green
    Write-Host "   Frontend: http://localhost:3000" -ForegroundColor Cyan
    Write-Host "   Backend API: http://localhost:8081" -ForegroundColor Cyan
    Write-Host "   PostgreSQL: localhost:5432" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "📋 Полезные команды:" -ForegroundColor Yellow
    Write-Host "   Просмотр логов: docker-compose logs" -ForegroundColor White
    Write-Host "   Остановка: docker-compose down" -ForegroundColor White
    Write-Host "   Перезапуск: docker-compose restart" -ForegroundColor White
} else {
    Write-Host "⚠️  Не все контейнеры запущены. Проверьте логи:" -ForegroundColor Yellow
    Write-Host "   docker-compose logs" -ForegroundColor White
}

Write-Host ""
Write-Host "🎉 Готово! Проект запущен в Docker." -ForegroundColor Green 