package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "День челленджа")
public class ChallengeDayDto {
    
    @Schema(description = "Номер дня", example = "1")
    private int dayNumber;
    
    @Schema(description = "Задание на день", example = "Медитировать 10 минут")
    private String task;
}