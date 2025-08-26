# Скрипт для остановки проекта SkillUp30 в Docker
# Автор: AI Assistant
# Дата: $(Get-Date -Format "yyyy-MM-dd")

Write-Host "🛑 Остановка проекта SkillUp30 в Docker..." -ForegroundColor Yellow

# Проверяем, находимся ли мы в правильной директории
if (-not (Test-Path "docker-compose.yml")) {
    Write-Host "❌ Файл docker-compose.yml не найден. Убедитесь, что вы находитесь в корневой директории проекта." -ForegroundColor Red
    exit 1
}

Write-Host "📁 Текущая директория: $(Get-Location)" -ForegroundColor Cyan

# Показываем текущий статус контейнеров
Write-Host "📊 Текущий статус контейнеров:" -ForegroundColor Yellow
docker-compose ps

# Спрашиваем, нужно ли удалить volumes
$removeVolumes = Read-Host "🗑️  Удалить данные базы данных (volumes)? (y/N)"
if ($removeVolumes -eq "y" -or $removeVolumes -eq "Y") {
    Write-Host "🗑️  Остановка контейнеров и удаление volumes..." -ForegroundColor Red
    docker-compose down -v
    Write-Host "✅ Контейнеры остановлены, данные БД удалены" -ForegroundColor Green
} else {
    Write-Host "🛑 Остановка контейнеров (данные БД сохранены)..." -ForegroundColor Yellow
    docker-compose down
    Write-Host "✅ Контейнеры остановлены, данные БД сохранены" -ForegroundColor Green
}

# Спрашиваем, нужно ли удалить образы
$removeImages = Read-Host "🗑️  Удалить Docker образы проекта? (y/N)"
if ($removeImages -eq "y" -or $removeImages -eq "Y") {
    Write-Host "🗑️  Удаление образов..." -ForegroundColor Red
    docker rmi skillup30_backend skillup30_frontend 2>$null
    Write-Host "✅ Образы удалены" -ForegroundColor Green
}

Write-Host ""
Write-Host "🎉 Проект остановлен!" -ForegroundColor Green 