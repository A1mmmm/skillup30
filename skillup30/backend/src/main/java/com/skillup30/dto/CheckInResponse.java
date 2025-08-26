package com.skillup30.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ на чек-ин")
public class CheckInResponse {
    
    @Schema(description = "Успешность операции", example = "true")
    private boolean success;
    
    @Schema(description = "Сообщение", example = "Чек-ин успешно выполнен")
    private String message;
    
    @Schema(description = "Количество завершенных дней", example = "15")
    private int completedDays;
    
    @Schema(description = "Завершен ли челлендж", example = "false")
    private boolean isCompleted;

    public CheckInResponse() {
    }

    public CheckInResponse(boolean success, String message, int completedDays, boolean isCompleted) {
        this.success = success;
        this.message = message;
        this.completedDays = completedDays;
        this.isCompleted = isCompleted;
    }
}