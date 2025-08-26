package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запись в лидерборде")
public class LeaderboardEntryDto {
    
    @Schema(description = "ID пользователя", example = "1")
    private Long userId;
    
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;
    
    @Schema(description = "Уровень пользователя", example = "5")
    private int level;
    
    @Schema(description = "XP пользователя", example = "1250")
    private int xp;
    
    @Schema(description = "Общее количество очков", example = "5000")
    private int totalPoints;
}