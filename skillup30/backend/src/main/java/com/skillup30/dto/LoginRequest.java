package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход")
public class LoginRequest {

    @NotBlank(message = "Имя пользователя или email обязательны")
    @Schema(description = "Имя пользователя или email", example = "john_doe", required = true)
    private String usernameOrEmail;

    @NotBlank(message = "Пароль обязателен")
    @Schema(description = "Пароль", example = "password123", required = true)
    private String password;
}