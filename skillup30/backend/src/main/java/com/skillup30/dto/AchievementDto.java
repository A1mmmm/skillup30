package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Достижение")
public class AchievementDto {

    @Schema(description = "ID достижения", example = "1")
    private Long id;

    @Schema(description = "Название достижения", example = "Первые шаги")
    private String name;

    @Schema(description = "Описание достижения", example = "Завершите свой первый челлендж")
    private String description;

    @Schema(description = "URL значка", example = "https://example.com/badge.png")
    private String badgeImage;

    @Schema(description = "Дата получения", example = "2024-01-15T10:30:00")
    private String awardedAt;
}