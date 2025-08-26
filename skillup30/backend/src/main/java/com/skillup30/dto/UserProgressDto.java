package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Прогресс пользователя")
public class UserProgressDto {
    
    @Schema(description = "ID челленджа", example = "1")
    private Long challengeId;
    
    @Schema(description = "Название челленджа", example = "30 дней медитации")
    private String challengeTitle;
    
    @Schema(description = "Количество завершенных дней", example = "15")
    private int completedDays;
    
    @Schema(description = "Завершен ли челлендж", example = "false")
    private boolean isCompleted;
}