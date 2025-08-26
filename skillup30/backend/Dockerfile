# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml
COPY pom.xml .

# Скачиваем зависимости (этот слой будет кэшироваться)
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Создаем пользователя для безопасности
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser

# Меняем владельца файлов
RUN chown -R javauser:javauser /app

# Переключаемся на пользователя
USER javauser

# Открываем порт
EXPOSE 8081

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"] 