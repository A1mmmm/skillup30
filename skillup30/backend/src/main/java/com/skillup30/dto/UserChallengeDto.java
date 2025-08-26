package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Челлендж пользователя")
public class UserChallengeDto {
    
    @Schema(description = "ID записи", example = "1")
    private Long id;
    
    @Schema(description = "Челлендж")
    private ChallengeDto challenge;
    
    @Schema(description = "Количество завершенных дней", example = "15")
    private int completedDays;
    
    @Schema(description = "Завершен ли челлендж", example = "false")
    private boolean isCompleted;
    
    @Schema(description = "Дата начала участия", example = "2024-01-15T10:30:00")
    private LocalDateTime startedAt;
    
    @Schema(description = "Дата завершения", example = "2024-02-14T10:30:00")
    private LocalDateTime completedAt;
} 