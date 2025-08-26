package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {
    
    @Schema(description = "Временная метка ошибки", example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP статус код", example = "400")
    private int status;
    
    @Schema(description = "Сообщение об ошибке", example = "Некорректные данные")
    private String message;
    
    @Schema(description = "Путь запроса", example = "/api/auth/register")
    private String path;
    
    public ErrorResponse(int status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}