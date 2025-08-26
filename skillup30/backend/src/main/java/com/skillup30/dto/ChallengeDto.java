package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Челлендж")
public class ChallengeDto {
    
    @Schema(description = "ID челленджа", example = "1")
    private Long id;
    
    @Schema(description = "Название челленджа", example = "30 дней медитации")
    private String title;
    
    @Schema(description = "Описание челленджа", example = "Ежедневная медитация по 10 минут")
    private String description;
    
    @Schema(description = "Очки награды", example = "100")
    private int rewardPoints;
    
    @Schema(description = "Уровень сложности", example = "Легкий", allowableValues = {"Легкий", "Средний", "Сложный"})
    private String difficultyLevel;
    
    @Schema(description = "URL обложки", example = "https://example.com/image.jpg")
    private String coverImage;
    
    @Schema(description = "Ежедневное задание", example = "Медитировать 10 минут")
    private String dailyTask;
}