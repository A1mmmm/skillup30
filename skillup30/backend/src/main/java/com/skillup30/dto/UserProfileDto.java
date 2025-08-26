package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Профиль пользователя")
public class UserProfileDto {
    
    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;
    
    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;
    
    @Schema(description = "Уровень пользователя", example = "5")
    private int level;
    
    @Schema(description = "Текущие XP", example = "1250")
    private int xp;
    
    @Schema(description = "Общее количество очков", example = "5000")
    private int totalPoints;
}